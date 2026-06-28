package com.subodh.InternshipPortal.wrapper;

import com.subodh.InternshipPortal.modals.Certificate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The type Certificate wrapper.
 */
@Slf4j
@Data
public class CertificateWrapper {
    private String certificateId;
    private String certificateFilePath;
    private String internshipName;
    private LocalDate startDate;
    private LocalDate completionDate;
    private String studentName;
    private String internshipDepartment;
    private String instructorName;
    private LocalDate issuedDate;
    private String verifyingURL;

    private String organizationLogoPath;

    private String officialSealPath;

    private String instructorSignaturePath;

    private String directorSignaturePath;

    /**
     * Instantiates a new Certificate wrapper.
     *
     * @param certificate             the certificate
     * @param organizationLogoPath    the organization logo path
     * @param officialSealPath        the official seal path
     * @param instructorSignaturePath the instructor signature path
     * @param directorSignaturePath   the director signature path
     * @param urlPrefix               the url prefix
     */
    public CertificateWrapper(
            Certificate certificate,
            String organizationLogoPath,
            String officialSealPath,
            String instructorSignaturePath,
            String directorSignaturePath,
            String urlPrefix) {
        this.certificateId = certificate.getCertificateId();
        this.certificateFilePath = certificate.getCertificateFilePath();
        this.internshipName = certificate.getInternshipStudents().getInternship().getInternshipName();
        this.startDate = certificate.getInternshipStudents().getInternship().getStartDate();
        this.completionDate = certificate.getInternshipStudents().getInternship().getEndDate();
        this.studentName = certificate.getInternshipStudents().getStudent().getUserName();
        this.internshipDepartment = certificate.getInternshipStudents().getInternship().getDepartment().getDepartmentName();
        this.instructorName = certificate.getInternshipStudents().getInternship().getCreatedBy().getUserName();
        this.issuedDate = certificate.getIssueDate();
        this.organizationLogoPath = organizationLogoPath;
        this.officialSealPath = officialSealPath;
        this.instructorSignaturePath = instructorSignaturePath;
        this.directorSignaturePath = directorSignaturePath;
        this.verifyingURL = urlPrefix + certificate.getCertificateId();



    }

    /**
     * Gets start date.
     *
     * @return the start date
     */
    public String getStartDate() {
        return formatDate(startDate);
    }

    /**
     * Gets completion date.
     *
     * @return the completion date
     */
    public String getCompletionDate() {
        return formatDate(completionDate);
    }

    /**
     * Gets issued date.
     *
     * @return the issued date
     */
    public String getIssuedDate() {
        return formatDate(issuedDate);
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
