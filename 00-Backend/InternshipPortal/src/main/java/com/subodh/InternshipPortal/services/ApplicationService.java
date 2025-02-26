package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.entities.Application;
import com.subodh.InternshipPortal.wrapper.ApplicationWrapper;

public interface ApplicationService {
    ApplicationWrapper save(Application application);


}
