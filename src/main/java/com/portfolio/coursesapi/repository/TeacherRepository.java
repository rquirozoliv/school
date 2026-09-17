package com.portfolio.coursesapi.repository;

import com.portfolio.coursesapi.entity.Course;
import com.portfolio.coursesapi.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
    Optional<Teacher> findByRut(String rut);

    // Busca estudiantes inscritos en un curso específico dentro de la tabla intermedia
    Page<Teacher> findByCourses_Id(Long courseId, Pageable pageable);
    //Optional<Course> findByCodeCourse(String code);

}
