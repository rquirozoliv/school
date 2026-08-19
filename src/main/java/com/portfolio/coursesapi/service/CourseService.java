package com.portfolio.coursesapi.service;

import com.portfolio.coursesapi.dto.request.CourseCreateRequest;
import com.portfolio.coursesapi.dto.request.CourseUpdateRequest;
import com.portfolio.coursesapi.dto.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    Page<CourseResponse> findAll(Pageable pageable);

    List<CourseResponse> findAllNoPaging();

    CourseResponse findById(Long id);

    CourseResponse create(CourseCreateRequest request);

    CourseResponse update(Long id, CourseUpdateRequest request);

    void delete(Long id);
}
