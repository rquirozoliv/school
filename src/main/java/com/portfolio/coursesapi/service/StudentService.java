package com.portfolio.coursesapi.service;

import com.portfolio.coursesapi.dto.request.StudentCreateRequest;
import com.portfolio.coursesapi.dto.request.StudentUpdateRequest;
import com.portfolio.coursesapi.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    /** courseId es opcional; si viene, filtra el listado paginado por curso. */
    Page<StudentResponse> findAll(Pageable pageable, Long courseId);

    List<StudentResponse> findAllNoPaging();

    StudentResponse findById(Long id);

    StudentResponse create(StudentCreateRequest request);

    StudentResponse update(Long id, StudentUpdateRequest request);

    void delete(Long id);
}
