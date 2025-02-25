package com.subodh.InternshipPortal.entities;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
public class Application {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    private String applicationTitle;



    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "internship_id", nullable = false)
    private Internship internship;


}
