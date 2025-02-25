package com.subodh.InternshipPortal.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Application {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    private String applicationTitle;

    @Lob
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "internship_id", nullable = false)
    private Internship internship;

    @ElementCollection
    @CollectionTable(name = "application_skills",joinColumns = @JoinColumn(name = "application_id"))
    private List<String> requiredSkills;
}
