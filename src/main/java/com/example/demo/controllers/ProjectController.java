package com.example.demo.controllers;

import com.example.demo.models.Project;
import com.example.demo.repositories.ProjectRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PostMapping("")
    public ResponseEntity<Project> create(@Valid @RequestBody Project entity) {
        log.info("REST request to save Project : {}", entity);

        try {
            Project result = projectRepository.save(entity);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating project: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("")
    public ResponseEntity<Project> update(@Valid @RequestBody Project entity) {
        log.info("REST request to update Project : {}", entity);

        try {
            Project result = projectRepository.save(entity);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating project", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Project>> query(@QuerydslPredicate(root = Project.class) Predicate predicate, Pageable pageable) {
        log.info("REST request to get Projects, predicate: {}, pageable: {}", predicate, pageable);

        boolean isEmptyPredicate = predicate == null ||
                (predicate instanceof BooleanBuilder && !((BooleanBuilder) predicate).hasValue());

        Page<Project> page = isEmptyPredicate
                ? projectRepository.findAll(pageable)
                : projectRepository.findAll(predicate, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));

        return ResponseEntity.ok().body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> get(@PathVariable Integer id) {
        log.info("REST request to get Project : {}", id);

        try {
            Project project = projectRepository.findById(id).orElse(null);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting project", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteSubject(@PathVariable Integer id) {
        log.info("REST request to delete Project : {}", id);

        try {
            projectRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error deleting project", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
