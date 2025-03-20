package com.subodh.InternshipPortal.services.Implementation;

import com.subodh.InternshipPortal.modals.Certificate;
import com.subodh.InternshipPortal.modals.InternshipStudents;
import com.subodh.InternshipPortal.repositories.CertificateRepository;
import com.subodh.InternshipPortal.repositories.InternshipStudentRepository;
import com.subodh.InternshipPortal.services.CertificateService;
import com.subodh.InternshipPortal.wrapper.APIRequest;
import com.subodh.InternshipPortal.wrapper.CertificateWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final InternshipStudentRepository internshipStudentRepository;
    private final CertificateRepository certificateRepository;

    @Value("${certificate.template.path}")
    private String certificateTemplatePath;

    public CertificateServiceImpl(InternshipStudentRepository internshipStudentRepository, CertificateRepository certificateRepository) {
        this.internshipStudentRepository = internshipStudentRepository;
        this.certificateRepository = certificateRepository;
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
        String certificateFileName = generateCertificateFile(certificate);
        certificate.setCertificateName(certificateFileName);
        certificateRepository.save(certificate);
        return new CertificateWrapper(certificate);
    }

    private String generateCertificateFile(Certificate certificate) {
        String fileName = certificate.getCertificateId()+".pdf";
        


    }
}
