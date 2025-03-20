package com.subodh.InternshipPortal.services.Implementation;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.subodh.InternshipPortal.modals.Certificate;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.repositories.CertificateRepository;
import com.subodh.InternshipPortal.repositories.InternshipStudentRepository;
import com.subodh.InternshipPortal.services.CertificateService;
import com.subodh.InternshipPortal.services.ProjectService;
import com.subodh.InternshipPortal.wrapper.APIRequest;
import com.subodh.InternshipPortal.wrapper.CertificateWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final InternshipStudentRepository internshipStudentRepository;
    private final CertificateRepository certificateRepository;
    private final ProjectService projectService;

    @Value("${certificate.template.path}")
    private String certificateTemplatePath;

    @Value("${file.storage.path}")
    private String rootFolderPath;

    public CertificateServiceImpl(InternshipStudentRepository internshipStudentRepository, CertificateRepository certificateRepository, ProjectService projectService) {
        this.internshipStudentRepository = internshipStudentRepository;
        this.certificateRepository = certificateRepository;
        this.projectService = projectService;
    }

    @Override
    public CertificateWrapper createCertificate(APIRequest<Long> request) {

        InternshipStudents internshipStudents = internshipStudentRepository.findById(request.getEntity()).orElse(null);
        Certificate certificate = new Certificate();
        certificate.setCertificateName("Certificate of Completion");
        certificate.setIssueDate(LocalDate.now());
        certificate.setInternshipStudents(internshipStudents);
        assert internshipStudents != null;
        certificate.setStudent(internshipStudents.getStudent());
        String certificateFileName = null;
        try {
            certificateFileName = generateCertificateFile(certificate);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        certificate.setCertificateName(certificateFileName);
        certificateRepository.save(certificate);
        return new CertificateWrapper(certificate);
    }

    private String generateCertificateFile(Certificate certificate) throws ExecutionException, InterruptedException {
        String fileName = certificate.getCertificateName() + ".pdf";

        // Get the main directory path where the student's data is stored
        String basePath = projectService.createFolder(
                rootFolderPath,
                certificate.getInternshipStudents().getInternship().getDepartment().getDepartmentName(),
                certificate.getInternshipStudents().getInternship().getInternshipName(),
                certificate.getStudent().getUserEmail()
        ).get(); // Ensure async execution completes

        // Define the certificate storage path
        String certificatePath = basePath + File.separator + "certificate";
        File certificateDir = new File(certificatePath);
        if (!certificateDir.exists()) {
            certificateDir.mkdirs();  // Create the "certificate" folder if it doesn't exist
        }

        // Final PDF file path
        String outputPath = certificatePath + File.separator + fileName;

        // Now generate the certificate PDF (implement this method separately)
        createPDFCertificate(certificate, outputPath);

        return outputPath; // Return the saved file path
    }

    private void createPDFCertificate(Certificate certificate, String outputPath) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Title
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD);
            Paragraph title = new Paragraph("Certificate of Completion", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Student name
            Font nameFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph studentName = new Paragraph("This is to certify that " + certificate.getStudent().getUserName(), nameFont);
            studentName.setAlignment(Element.ALIGN_CENTER);
            document.add(studentName);

            // Internship details
            Font detailFont = new Font(Font.FontFamily.TIMES_ROMAN, 14);
            Paragraph details = new Paragraph(
                    "has successfully completed the internship titled '" +
                            certificate.getInternshipStudents().getInternship().getInternshipName() +
                            "' at Govt of Sikkim from " +
                            certificate.getInternshipStudents().getInternship().getStartDate() +
                            " to " + certificate.getInternshipStudents().getInternship().getEndDate() + ".", detailFont
            );
            details.setAlignment(Element.ALIGN_CENTER);
            document.add(details);

            // Issue Date
            Paragraph date = new Paragraph("Issued on: " + certificate.getIssueDate(), detailFont);
            date.setAlignment(Element.ALIGN_CENTER);
            document.add(date);

            document.close();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Error generating PDF certificate", e);
        }
    }

}
