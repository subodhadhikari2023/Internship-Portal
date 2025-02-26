package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.enums.StudentApplicationStatus;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;

import java.util.List;

public interface ApplicationService {
    ApplicationWrapper save(Application application);


    List<ApplicationWrapper> findAllofUser();


    ApplicationWrapper reviewApplications(StudentApplicationStatus status, Long applicationId);
}
