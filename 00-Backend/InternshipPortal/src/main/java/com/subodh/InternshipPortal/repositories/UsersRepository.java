package com.subodh.InternshipPortal.repositories;



import com.subodh.InternshipPortal.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;


public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUserEmail(String userEmail);


}
