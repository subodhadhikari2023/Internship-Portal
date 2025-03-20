package com.subodh.InternshipPortal.services;


import com.subodh.InternshipPortal.wrapper.APIRequest;
import com.subodh.InternshipPortal.wrapper.CertificateWrapper;
import org.springframework.http.HttpStatus;

public interface CertificateService {
    CertificateWrapper createCertificate(APIRequest<Long> request);
}
