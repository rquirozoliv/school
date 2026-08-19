package com.portfolio.coursesapi.repository;

import com.portfolio.coursesapi.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRut(String rut);

    boolean existsByRut(String rut);

    Page<Student> findByCourseId(Long courseId, Pageable pageable);
}
