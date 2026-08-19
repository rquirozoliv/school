package com.portfolio.coursesapi.service.impl;

import com.portfolio.coursesapi.dto.request.CourseCreateRequest;
import com.portfolio.coursesapi.dto.request.CourseUpdateRequest;
import com.portfolio.coursesapi.dto.response.CourseResponse;
import com.portfolio.coursesapi.entity.Course;
import com.portfolio.coursesapi.exception.DuplicateResourceException;
import com.portfolio.coursesapi.exception.ResourceNotFoundException;
import com.portfolio.coursesapi.mapper.CourseMapper;
import com.portfolio.coursesapi.repository.CourseRepository;
import com.portfolio.coursesapi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable).map(CourseMapper.TO_RESPONSE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findAllNoPaging() {
        return courseRepository.findAll().stream()
                .map(CourseMapper.TO_RESPONSE)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse findById(Long id) {
        return courseRepository.findById(id)
                .map(CourseMapper.TO_RESPONSE)
                .orElseThrow(() -> notFound(id));
    }

    @Override
    @Transactional
    public CourseResponse create(CourseCreateRequest request) {
        String normalizedCode = request.code().trim().toUpperCase();
        if (courseRepository.existsByCodeIgnoreCase(normalizedCode)) {
            throw new DuplicateResourceException("Ya existe un curso con el codigo " + normalizedCode);
        }
        Course saved = courseRepository.save(CourseMapper.toEntity(request));
        return CourseMapper.TO_RESPONSE.apply(saved);
    }

    @Override
    @Transactional
    public CourseResponse update(Long id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(() -> notFound(id));

        if (request.code() != null && !request.code().isBlank()) {
            String normalizedCode = request.code().trim().toUpperCase();
            courseRepository.findByCodeIgnoreCase(normalizedCode)
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new DuplicateResourceException("Ya existe un curso con el codigo " + normalizedCode);
                    });
        }

        CourseMapper.applyUpdate(course, request);
        return CourseMapper.TO_RESPONSE.apply(courseRepository.save(course));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw notFound(id);
        }
        courseRepository.deleteById(id);
    }

    private ResourceNotFoundException notFound(Long id) {
        return new ResourceNotFoundException("Curso con id " + id + " no fue encontrado");
    }
}
