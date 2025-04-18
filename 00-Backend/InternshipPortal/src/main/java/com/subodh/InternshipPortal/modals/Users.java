package com.subodh.InternshipPortal.modals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The type Users.
 */
@Entity
@Getter
@Setter
@ToString
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Digits(integer = 15, fraction = 0, message = "Phone number must be a valid number with at most 15 digits")
    private Long userPhoneNumber;

        @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Roles> roles;
//    @ManyToOne
//    @JsonManagedReference
//    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
//    Roles role;


    @ElementCollection
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "student_skills")
    private Set<String> studentSkills;

    /**
     * The Department.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    DepartmentDetails department;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "education_id")
    @JsonBackReference
    private Education education;

    /**
     * The Resume file path.
     */
    String resumeFilePath;

    /**
     * The Profile photo file path.
     */
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'profile.png'")
    String profilePhotoFilePath;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<InternshipStudents> enrolledInternships;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Project> projects;


    /**
     * Add role.
     *
     * @param roleName the role name
     */
    public void addRole(String roleName) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        Roles role = new Roles();
        role.setRoleName(roleName);
        role.setUser(this);
        this.roles.add(role);
    }


}
