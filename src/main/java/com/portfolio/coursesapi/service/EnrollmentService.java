package com.portfolio.coursesapi.service;

import com.portfolio.coursesapi.entity.Course;
import com.portfolio.coursesapi.entity.Student;

public interface EnrollmentService {
    Course matricular(Student student);
}
