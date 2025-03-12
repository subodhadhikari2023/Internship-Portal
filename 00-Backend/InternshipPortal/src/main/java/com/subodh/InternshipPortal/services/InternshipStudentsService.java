package com.subodh.InternshipPortal.services;

import com.subodh.InternshipPortal.wrapper.InternshipStudentsWrapper;

import java.util.List;

public interface InternshipStudentsService {
    List<InternshipStudentsWrapper> findAllByStudentId(Long userId);
}
