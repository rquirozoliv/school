package com.portfolio.coursesapi.service.impl;

import com.portfolio.coursesapi.dto.request.TeacherCreateRequest;
import com.portfolio.coursesapi.dto.request.TeacherResponseDto;
import com.portfolio.coursesapi.dto.request.TeacherUpdateRequest;
import com.portfolio.coursesapi.entity.Teacher;
import com.portfolio.coursesapi.exception.GlobalExceptionHandler;
import com.portfolio.coursesapi.mapper.StudentMapper;
import com.portfolio.coursesapi.mapper.TeacherMapper;
import com.portfolio.coursesapi.repository.CourseRepository;
import com.portfolio.coursesapi.repository.TeacherRepository;
import com.portfolio.coursesapi.service.TeacherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;


    @Override
    @Transactional
    public Page<TeacherResponseDto> findAll(Pageable pageable, Long courseId) {
        Page<Teacher> page = Optional.ofNullable(courseId)
                .map(id -> teacherRepository.findByCourses_Id(id, pageable)) // Busca en la relación ManyToMany
                .orElseGet(() -> teacherRepository.findAll(pageable));
        return page.map(TeacherMapper.TO_RESPONSE);
    }

    @Override
    public List<TeacherResponseDto> findAllNoPaging() {
     return teacherRepository.findAll().stream()
             .map(TeacherMapper.TO_RESPONSE)
             .toList();
    }

    @Override
    public TeacherResponseDto findById(Long id) {
        return teacherRepository.findById(id)
                .map(TeacherMapper.TO_RESPONSE)
                .orElseThrow(() -> GlobalExceptionHandler.notFound(id));
    }

    @Override
    public TeacherResponseDto create(TeacherCreateRequest request) {
        return null;
    }

    @Override
    public TeacherResponseDto update(Long id, TeacherUpdateRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
