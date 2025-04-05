package com.subodh.InternshipPortal.services;


import com.subodh.InternshipPortal.wrapper.CertificateWrapper;

/**
 * The interface Certificate service.
 */
public interface CertificateService {
    /**
     * Create certificate certificate wrapper.
     *
     * @param request the request
     * @return the certificate wrapper
     */
    CertificateWrapper createCertificate(Long request);
}
