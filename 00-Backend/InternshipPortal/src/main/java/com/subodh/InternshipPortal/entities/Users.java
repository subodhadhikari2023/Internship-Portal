package com.subodh.InternshipPortal.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name",nullable = false,unique = false)
    private String userName;

    @Column(name = "user_password",nullable = false)
    private String userPassword;

    @Column(name = "user_email",nullable = false,unique = true)
    private String userEmail;

    @Column(name = "user_phone_number",nullable = false,unique = true)
    private Number userPhoneNumber;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    @JsonManagedReference
    private List<Roles> roles;


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
