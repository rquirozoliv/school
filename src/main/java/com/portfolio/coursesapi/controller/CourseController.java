package com.portfolio.coursesapi.controller;

import com.portfolio.coursesapi.dto.request.CourseCreateRequest;
import com.portfolio.coursesapi.dto.request.CourseUpdateRequest;
import com.portfolio.coursesapi.dto.response.CourseResponse;
import com.portfolio.coursesapi.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /** GET /courses -> lista paginada (?page=&size=&sort=). */
    @GetMapping
    public ResponseEntity<Page<CourseResponse>> findAll(
            @PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(courseService.findAll(pageable));
    }

    /** GET /courses/all -> lista completa sin paginar. */
    @GetMapping("/all")
    public ResponseEntity<List<CourseResponse>> findAllNoPaging() {
        return ResponseEntity.ok(courseService.findAllNoPaging());
    }

    /** GET /courses/:id -> 200 con el curso, o 404 si no existe. */
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    /** POST /courses -> 201 si se crea, 400 si el JSON es invalido. */
    @PostMapping
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseCreateRequest request) {
        CourseResponse created = courseService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /** PUT /courses/:id -> actualiza solo los campos incluidos en el JSON. */
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody CourseUpdateRequest request) {
        return ResponseEntity.ok(courseService.update(id, request));
    }

    /** DELETE /courses/:id -> 200 si se elimina, 404 si no existe. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok().build();
    }
}
