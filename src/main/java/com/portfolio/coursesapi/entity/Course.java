package com.portfolio.coursesapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "students")
@EqualsAndHashCode(of = "code")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código alfanumérico único del curso (ej: CS-101) */
    @Column(nullable = false, length = 10, unique = true)
    private String code;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Integer credits;

    @Column(length = 500)
    private String description;

    @Builder.Default
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}
