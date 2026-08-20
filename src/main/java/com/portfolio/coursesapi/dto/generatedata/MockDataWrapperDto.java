package com.portfolio.coursesapi.dto.generatedata;

import java.util.List;

public record MockDataWrapperDto(
        List<CourseMockDto> courses,
        List<StudentMockDto> students
) {}
