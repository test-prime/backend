package com.example.demo.controller;

import com.example.demo.dto.QueryResult;
import com.example.demo.entity.Project;
import com.example.demo.service.ProjectService;
import com.querydsl.core.types.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    private ProjectService projectService;

    @PostMapping("")
    public ResponseEntity<Project> create(@Valid @RequestBody Project body) {
        log.info("REST request to save Project : {}", body);

        try {
            Project entity = projectService.create(body);
            return new ResponseEntity<>(entity, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating project: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("")
    public ResponseEntity<Project> update(@Valid @RequestBody Project body) {
        log.info("REST request to update Project : {}", body);

        try {
            Project entity = projectService.update(body);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating project", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Project>> findAll(HttpServletRequest request, @QuerydslPredicate(root = Project.class) Predicate predicate, Pageable pageable) {
        log.info("REST request to get Projects, predicate: {}, pageable: {}", predicate, pageable);

        String queryString = request.getQueryString();
        QueryResult<Project> result = projectService.findAll(predicate, pageable, queryString);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotal()));

        return ResponseEntity.ok().body(result.getEntities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> get(@PathVariable Integer id) {
        log.info("REST request to get Project : {}", id);

        try {
            Project entity = projectService.findById(id).orElse(null);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting project", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> delete(@PathVariable Integer id) {
        log.info("REST request to delete Project : {}", id);

        try {
            projectService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error deleting project", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
