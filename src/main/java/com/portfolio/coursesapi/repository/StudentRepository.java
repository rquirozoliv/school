package com.portfolio.coursesapi.repository;

import com.portfolio.coursesapi.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
    Optional<Student> findByRut(String rut);

    // Busca estudiantes inscritos en un curso específico dentro de la tabla intermedia
    Page<Student> findByCourses_Id(Long courseId, Pageable pageable);

}
