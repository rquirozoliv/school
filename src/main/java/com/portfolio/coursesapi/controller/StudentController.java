package com.portfolio.coursesapi.controller;

import com.portfolio.coursesapi.dto.request.StudentCreateRequest;
import com.portfolio.coursesapi.dto.request.StudentUpdateRequest;
import com.portfolio.coursesapi.dto.response.StudentResponse;
import com.portfolio.coursesapi.service.StudentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /** GET /students -> lista paginada (?page=&size=&sort=), filtrable por ?courseId=. */
    @GetMapping
    public ResponseEntity<Page<StudentResponse>> findAll(
            @PageableDefault(sort = "id") Pageable pageable,
            @RequestParam(required = false) Long courseId) {
        return ResponseEntity.ok(studentService.findAll(pageable, courseId));
    }

    /** GET /students/all -> lista completa sin paginar. */
    @GetMapping("/all")
    public ResponseEntity<List<StudentResponse>> findAllNoPaging() {
        return ResponseEntity.ok(studentService.findAllNoPaging());
    }

    /** GET /students/:id -> 200 con el alumno, o 404 si no existe. */
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    /** POST /students -> 201 si se crea, 400 si el JSON es invalido. */
    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentCreateRequest request) {
        StudentResponse created = studentService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /** PUT /students/:id -> actualiza solo los campos incluidos en el JSON. */
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody StudentUpdateRequest request) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    /** DELETE /students/:id -> 200 si se elimina, 404 si no existe. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
