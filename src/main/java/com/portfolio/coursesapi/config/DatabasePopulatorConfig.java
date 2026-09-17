package com.portfolio.coursesapi.config;

import com.portfolio.coursesapi.dto.generatedata.MockDataWrapperDto;
import com.portfolio.coursesapi.dto.generatedata.TeacherMockDto;
import com.portfolio.coursesapi.entity.Course;
import com.portfolio.coursesapi.entity.Student;
import com.portfolio.coursesapi.entity.Teacher;
import com.portfolio.coursesapi.repository.CourseRepository;
import com.portfolio.coursesapi.repository.StudentRepository;
import com.portfolio.coursesapi.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabasePopulatorConfig {

    private final ResourceLoader resourceLoader;

    @Bean
    CommandLineRunner populateDatabase(
            CourseRepository courseRepository,
            StudentRepository studentRepository,
            TeacherRepository teacherRepository,
            ObjectMapper objectMapper) {

        return args -> {
            // Verificar si AMBAS tablas ya tienen datos para no hacer nada
            if (courseRepository.count() > 0 && studentRepository.count() > 0 && teacherRepository.count() > 0) {
                log.info("Cursos y Estudiantes ya existen en la base de datos. Saltando población inicial.");
                return;
            }

            log.info("Iniciando proceso selectivo de población de base de datos...");
            Resource resource = resourceLoader.getResource("classpath:mock-data.json");

            try (InputStream inputStream = resource.getInputStream()) {
                MockDataWrapperDto mockData = objectMapper.readValue(inputStream, MockDataWrapperDto.class);
                Map<Long, Course> courseCache = new HashMap<>();

                // === FASE PROCESAR TEACHER ===
                if (teacherRepository.count() == 0) {
                    log.info("La tabla de estudiantes está vacía. Insertando estudiantes...");
                    int teacherCount = 0;

                    for (var teacherDto : mockData.teachers()) {
                        // Separamos el nombre completo del JSON en Nombre y Apellido
                        String fullName = teacherDto.name();
                        String firstName = fullName;
                        String lastName = "N/A"; // Valor por defecto seguro si no tiene espacio

                        if (fullName != null && fullName.contains(" ")) {
                            int firstSpaceIndex = fullName.indexOf(" ");
                            firstName = fullName.substring(0, firstSpaceIndex);
                            lastName = fullName.substring(firstSpaceIndex + 1);
                        }

                        Teacher teacher = Teacher.builder()
                                .name(firstName)       // Guardamos el primer nombre
                                .lastname(lastName)    // Guardamos el apellido extraído 👈
                                .email(teacherDto.email())
                                .enrollmentDate(teacherDto.enrollmentDate())
                                .build();

                        teacherCount =
                                getTeacherCount(teacherRepository, courseCache, teacherCount, teacher,
                                        teacherDto.courseIds());
                    }
                    log.info("Se han cargado {} estudiantes con sus respectivas inscripciones.", teacherCount);
                } else {
                    log.info("La tabla de estudiantes ya contenía datos. No se realizaron inserciones de alumnos.");
                }

                log.info("Proceso de verificación y población finalizado con éxito.");

                // === FASE 1: PROCESAR CURSOS ===
                if (courseRepository.count() == 0) {
                    log.info("La tabla de cursos está vacía. Insertando cursos desde el JSON...");
                    for (var courseDto : mockData.courses()) {
                        Course course = Course.builder()
                                .code(courseDto.code())
                                .title(courseDto.title())
                                .credits(courseDto.credits())
                                .description(courseDto.description())
                                .build();

                        Course savedCourse = courseRepository.save(course);
                        courseCache.put(courseDto.id(), savedCourse);
                    }
                    log.info("Se han cargado {} cursos exitosamente.", courseCache.size());
                } else {
                    log.info("Los cursos ya existen en la BD. Cargando referencias en memoria para los estudiantes...");
                    // Si ya existen en BD, mapeamos el "code" o los buscamos para asociar los IDs del JSON
                    for (var courseDto : mockData.courses()) {
                        courseRepository.findByCode(courseDto.code()).ifPresent(existingCourse -> {
                            courseCache.put(courseDto.id(), existingCourse);
                        });
                    }
                }

                // === FASE 2: PROCESAR ESTUDIANTES ===
                if (studentRepository.count() == 0) {
                    log.info("La tabla de estudiantes está vacía. Insertando estudiantes...");
                    int studentCount = 0;

                    for (var studentDto : mockData.students()) {
                        // Separamos el nombre completo del JSON en Nombre y Apellido
                        String fullName = studentDto.name();
                        String rutFromMockData = studentDto.rut();
                        String firstName = fullName;
                        String lastName = "N/A"; // Valor por defecto seguro si no tiene espacio

                        if (fullName != null && fullName.contains(" ")) {
                            int firstSpaceIndex = fullName.indexOf(" ");
                            firstName = fullName.substring(0, firstSpaceIndex);
                            lastName = fullName.substring(firstSpaceIndex + 1);
                        }

                        Student student = Student.builder()
                                .name(firstName)       // Guardamos el primer nombre
                                .lastname(lastName)    // Guardamos el apellido extraído 👈
                                .email(studentDto.email())
                                .age(studentDto.age())
                                .rut(rutFromMockData)
                                .enrollmentDate(studentDto.enrollmentDate())
                                .build();

                        studentCount = getStudentCount(studentRepository, courseCache, studentCount,
                                student, studentDto.courseIds());
                    }
                    log.info("Se han cargado {} estudiantes con sus respectivas inscripciones.", studentCount);
                } else {
                    log.info("La tabla de estudiantes ya contenía datos. No se realizaron inserciones de alumnos.");
                }

                log.info("Proceso de verificación y población finalizado con éxito.");

            } catch (Exception e) {
                log.error("Error crítico al intentar poblar la base de datos desde el archivo JSON", e);
            }
        };
    }

    private int getTeacherCount(TeacherRepository teacherRepository, Map<Long, Course> courseCache,
                                int teacherCount, Teacher teacher, List<Long> longs) {
        if (longs != null) {
            for (Long jsonCourseId : longs) {
                Course matchedCourse = courseCache.get(jsonCourseId);
                if (matchedCourse != null) {
                    teacher.addCourse(matchedCourse);
                }
            }
        }

        teacherRepository.save(teacher);
        teacherCount++;
        return teacherCount;
    }

    private int getStudentCount(StudentRepository studentRepository, Map<Long, Course> courseCache,
                                int studentCount, Student student, List<Long> longs) {
        if (longs != null) {
            for (Long jsonCourseId : longs) {
                Course matchedCourse = courseCache.get(jsonCourseId);
                if (matchedCourse != null) {
                    student.addCourse(matchedCourse);
                }
            }
        }

        studentRepository.save(student);
        studentCount++;
        return studentCount;
    }

    private Teacher buildTeacherFromTeacherMockDto(TeacherMockDto teacherMockDto){
        return Teacher.builder()
                .id(teacherMockDto.id())
                .name(teacherMockDto.name())
                .email(teacherMockDto.email())
                .enrollmentDate(teacherMockDto.enrollmentDate())
                .build();
    }
}
