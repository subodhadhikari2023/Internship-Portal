package com.subodh.InternshipPortal.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class APIRequest<T> {
    T entity;
}
