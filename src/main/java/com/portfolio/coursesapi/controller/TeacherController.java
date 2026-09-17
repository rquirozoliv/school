package com.portfolio.coursesapi.controller;

import com.portfolio.coursesapi.dto.request.*;
import com.portfolio.coursesapi.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    /** GET /students -> lista paginada (?page=&size=&sort=), filtrable por ?courseId=. */
    /*@GetMapping
    public ResponseEntity<Page<StudentResponse>> findAll(
            @PageableDefault(sort = "id") Pageable pageable,
            @RequestParam(required = false) Long courseId) {
        return ResponseEntity.ok(teacherService.findAll(pageable, courseId));
    }*/

    /**
     * GET /students/all -> lista completa sin paginar.
     */
    @GetMapping("/all")
    public ResponseEntity<List<TeacherResponseDto>> findAllNoPaging() {
        return ResponseEntity.ok(teacherService.findAllNoPaging());
    }

    /**
     * GET /students/:id -> 200 con el alumno, o 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.findById(id));
    }

    /**
     * POST /students -> 201 si se crea, 400 si el JSON es invalido.
     */
    @PostMapping
    public ResponseEntity<TeacherResponseDto> create(@Valid @RequestBody TeacherCreateRequest request) {
        TeacherResponseDto created = teacherService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /**
     * PUT /students/:id -> actualiza solo los campos incluidos en el JSON.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody TeacherUpdateRequest request) {
        return ResponseEntity.ok(teacherService.update(id, request));
    }

    /** DELETE /students/:id -> 200 si se elimina, 404 si no existe. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.ok().build();
    }
}
