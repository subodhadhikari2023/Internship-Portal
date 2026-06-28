package com.subodh.InternshipPortal.services.Implementation;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.subodh.InternshipPortal.enums.StudentInternshipStatus;
import com.subodh.InternshipPortal.exceptions.DuplicateCertificateClaimException;
import com.subodh.InternshipPortal.modals.Certificate;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.repositories.CertificateRepository;
import com.subodh.InternshipPortal.repositories.InternshipStudentRepository;
import com.subodh.InternshipPortal.services.CertificateService;
import com.subodh.InternshipPortal.services.ProjectService;
import com.subodh.InternshipPortal.wrapper.CertificateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

/**
 * The type Certificate service.
 */
@Slf4j
@Service
public class CertificateServiceImpl implements CertificateService {
    private final InternshipStudentRepository internshipStudentRepository;
    private final CertificateRepository certificateRepository;
    private final ProjectService projectService;

    @Value("${certificate.template.path}")
    private String certificateTemplatePath;


    @Value("${file.storage.path}")
    private String rootFolderPath;

    @Value("${organization.logo.path}")
    private String organizationLogoPath;

    @Value("${official.seal.path}")
    private String officialSealPath;

    @Value("${instructor.signature.path}")
    private String instructorSignaturePath;

    @Value("${director.signature.path}")
    private String directorSignaturePath;

    @Value("${url.prefix}")
    private String urlPrefix;

    /**
     * Instantiates a new Certificate service.
     *
     * @param internshipStudentRepository the internship student repository
     * @param certificateRepository       the certificate repository
     * @param projectService              the project service
     */
    public CertificateServiceImpl(InternshipStudentRepository internshipStudentRepository, CertificateRepository certificateRepository, ProjectService projectService) {
        this.internshipStudentRepository = internshipStudentRepository;
        this.certificateRepository = certificateRepository;
        this.projectService = projectService;
    }

    @Override
    @Transactional
    public CertificateWrapper createCertificate(Long request) {
        InternshipStudents internshipStudents = internshipStudentRepository.findById(request).orElse(null);
        if (certificateRepository.existsByInternshipStudents(internshipStudents)) {
            throw new DuplicateCertificateClaimException("Certificate already claimed for this internship.");
        }

        assert internshipStudents != null;
        if (internshipStudents.getStatus()!= StudentInternshipStatus.COMPLETED){
            throw new DuplicateCertificateClaimException("Internship hasn't been completed yet.");
        }

        Certificate certificate = new Certificate();
        certificate.setCertificateFilePath("Certificate of Completion");
        certificate.setIssueDate(LocalDate.now());
        certificate.setInternshipStudents(internshipStudents);
        certificate.setStudent(internshipStudents.getStudent());

        try {
            Certificate saved = certificateRepository.save(certificate);
            return generateCertificateFile(saved);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private CertificateWrapper generateCertificateFile(Certificate certificate) throws ExecutionException, InterruptedException {
        String fileName = certificate.getCertificateId() + ".pdf";

        // Get the main directory path where the student's data is stored
        String basePath = projectService.createFolder(
                rootFolderPath,
                certificate.getInternshipStudents().getInternship().getDepartment().getDepartmentName(),
                certificate.getInternshipStudents().getInternship().getInternshipName(),
                certificate.getStudent().getUserEmail()
        ).get(); // Ensure async execution completes

        if (basePath != null) {
            String certificatePath = basePath + File.separator + "certificate";
            File certificateDir = new File(certificatePath);
            if (!certificateDir.exists()) {
                certificateDir.mkdirs();  // Create the "certificate" folder if it doesn't exist
            }
            String outputPath = certificatePath + File.separator + fileName;

            // Create CertificateWrapper object
            CertificateWrapper certificateWrapper = new CertificateWrapper(
                    certificate, organizationLogoPath, officialSealPath, instructorSignaturePath, directorSignaturePath, urlPrefix
            );

            // Generate the PDF certificate
            createPDFCertificate(certificateWrapper, outputPath);
            String relativePath = outputPath.replaceFirst("^" + rootFolderPath, "/storage/Internship-Portal");
            certificate.setCertificateFilePath(relativePath);


            return new CertificateWrapper(certificateRepository.save(certificate),organizationLogoPath,officialSealPath,instructorSignaturePath,directorSignaturePath,urlPrefix);
        }
        return null;
    }


    /**
     * Create pdf certificate.
     *
     * @param certificateWrapper the certificate wrapper
     * @param outputPath         the output path
     */
    public static void createPDFCertificate(CertificateWrapper certificateWrapper, String outputPath) {
        Document document = new Document(PageSize.A4.rotate()); // Set landscape mode
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Define fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 32, Font.BOLD, BaseColor.BLACK);
            Font nameFont = new Font(Font.FontFamily.HELVETICA, 26, Font.BOLD, new BaseColor(0, 51, 102)); // Dark Blue
            Font textFont = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, BaseColor.BLACK);
            Font dateFont = new Font(Font.FontFamily.HELVETICA, 14, Font.ITALIC, BaseColor.DARK_GRAY);
            Font linkFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(0, 102, 204)); // Blue

            // Gradient Background
            PdfContentByte canvas = writer.getDirectContentUnder();
            Rectangle rect = new Rectangle(document.getPageSize());
            rect.setBackgroundColor(new BaseColor(230, 230, 230)); // Light gray background
            canvas.rectangle(rect);

            // Organization Logo
            Image logo = Image.getInstance(certificateWrapper.getOrganizationLogoPath());
            logo.scaleToFit(100, 100);
            logo.setAbsolutePosition(40, document.getPageSize().getHeight() - 120);
            document.add(logo);

            // Verification Link at Top Right
            Paragraph verifyLink = new Paragraph("Verify: " + certificateWrapper.getVerifyingURL(), linkFont);
            verifyLink.setAlignment(Element.ALIGN_RIGHT);
            verifyLink.setSpacingAfter(20);
            document.add(verifyLink);

            // Title
            Paragraph title = new Paragraph("CERTIFICATE OF COMPLETION", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Student Name
            Paragraph studentName = new Paragraph("This is to certify that", textFont);
            studentName.setAlignment(Element.ALIGN_CENTER);
            document.add(studentName);

            Paragraph name = new Paragraph(certificateWrapper.getStudentName(), nameFont);
            name.setAlignment(Element.ALIGN_CENTER);
            name.setSpacingAfter(10);
            document.add(name);

            // Internship Details
            Paragraph details = new Paragraph(
                    "has successfully completed the internship titled '" + certificateWrapper.getInternshipName() +
                            "' in the " + certificateWrapper.getInternshipDepartment() +
                            " department under the guidance of " + certificateWrapper.getInstructorName() + ".",
                    textFont
            );
            details.setAlignment(Element.ALIGN_CENTER);
            details.setSpacingBefore(10);
            document.add(details);

            // Fix: Use preformatted dates directly
            Paragraph duration = new Paragraph(
                    "Internship Duration: " + certificateWrapper.getStartDate() +
                            " to " + certificateWrapper.getCompletionDate(),
                    textFont
            );

            duration.setAlignment(Element.ALIGN_CENTER);
            duration.setSpacingBefore(10);
            document.add(duration);

            // Issue Date
            Paragraph issueDate = new Paragraph("Issued on: " + certificateWrapper.getIssuedDate(), dateFont);
            issueDate.setAlignment(Element.ALIGN_CENTER);
            issueDate.setSpacingBefore(20);
            document.add(issueDate);

            // Official Seal
            Image seal = Image.getInstance(certificateWrapper.getOfficialSealPath());
            seal.scaleToFit(100, 100);
            seal.setAbsolutePosition(450, 100);
            document.add(seal);

            // Signatures
            Image instructorSignature = Image.getInstance(certificateWrapper.getInstructorSignaturePath());
            instructorSignature.scaleToFit(120, 50);
            instructorSignature.setAbsolutePosition(150, 70);
            document.add(instructorSignature);

            Image directorSignature = Image.getInstance(certificateWrapper.getDirectorSignaturePath());
            directorSignature.scaleToFit(120, 50);
            directorSignature.setAbsolutePosition(600, 70);
            document.add(directorSignature);

            // Signature Labels
            Paragraph instructorLabel = new Paragraph("Instructor", textFont);
            instructorLabel.setAlignment(Element.ALIGN_LEFT);
            instructorLabel.setIndentationLeft(150);
            instructorLabel.setSpacingBefore(30);
            document.add(instructorLabel);

            Paragraph directorLabel = new Paragraph("Director", textFont);
            directorLabel.setAlignment(Element.ALIGN_RIGHT);
            directorLabel.setIndentationRight(150);
            document.add(directorLabel);

            document.close();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generating PDF certificate", e);
        }
    }

}
