package com.subodh.InternshipPortal.modals;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity

public class DepartmentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    private String departmentName;

}
