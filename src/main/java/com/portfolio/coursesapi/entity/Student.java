package com.portfolio.coursesapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(name = "uk_students_rut", columnNames = "rut"),
        @UniqueConstraint(name = "uk_students_email", columnNames = "email")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "courses") // Evita recursividad infinita en logs
@EqualsAndHashCode(of = "email") // Identidad basada en el email único de negocio
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** RUT normalizado, formato "12345678-5". Opcional para la carga inicial de datos. */
    @Column(length = 12)
    private String rut;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String lastname;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE}) // 👈 Cambiado solo a MERGE
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id",
                    foreignKey = @ForeignKey(name = "fk_student_courses_student")),
            inverseJoinColumns = @JoinColumn(name = "course_id",
                    foreignKey = @ForeignKey(name = "fk_student_courses_course"))
    )
    private Set<Course> courses = new HashSet<>();

    /**
     * Método helper indispensable en producción para mantener la sincronización bidireccional de la relación.
     */
    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }
}
