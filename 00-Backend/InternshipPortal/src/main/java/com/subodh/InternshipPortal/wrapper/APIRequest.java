package com.subodh.InternshipPortal.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The type Api request.
 *
 * @param <T> the type parameter
 */
@Data
@AllArgsConstructor
@Builder
public class APIRequest<T> {
    /**
     * The Entity.
     */
    T entity;
}
