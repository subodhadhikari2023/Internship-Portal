package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.Certificate;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class CertificateWrapper {
    private String certificateId;
    private String certificateName;
    private String internshipName;
    private LocalDate startDate;
    private LocalDate completionDate;
    private String studentName;
    private String internshipDepartment;
    private String instructorName;
    private LocalDate issuedDate;

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


    private String verifyingURL;

    public CertificateWrapper(Certificate certificate) {
        this.certificateId = certificate.getCertificateId();
        this.internshipName = certificate.getInternshipStudents().getInternship().getInternshipName();
        this.startDate = certificate.getInternshipStudents().getInternship().getStartDate();
        this.completionDate = certificate.getInternshipStudents().getInternship().getEndDate();
        this.studentName = certificate.getInternshipStudents().getStudent().getUserName();
        this.internshipDepartment = certificate.getInternshipStudents().getInternship().getDepartment().getDepartmentName();
        this.instructorName = certificate.getInternshipStudents().getInternship().getCreatedBy().getUserName();
        this.issuedDate = certificate.getIssueDate();
        this.verifyingURL = UriComponentsBuilder.fromUriString(this.urlPrefix).queryParam("certificateId", this.certificateId).toUriString();


    }

    public String getStartDate() {
        return formatDate(startDate);
    }

    public String getCompletionDate() {
        return formatDate(completionDate);
    }

    private String formatDate(LocalDate date) {
        if (date == null) return null;

        int day = date.getDayOfMonth();
        String suffix = getDaySuffix(day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy"); // Example: June 2023

        return day + suffix + " " + date.format(formatter);  // "15th June 2023"
    }

    private String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";  // Special case: 11th, 12th, 13th
        }
        return switch (day % 10) {
            case 1 -> "st"; // 1st, 21st
            case 2 -> "nd"; // 2nd, 22nd
            case 3 -> "rd"; // 3rd, 23rd
            default -> "th"; // 4th, 5th, ..., 24th, 25th
        };
    }
}
