
package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
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
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping("")
    public ResponseEntity<Task> create(@Valid @RequestBody Task body) {
        log.info("REST request to save Task : {}", body);

        try {
            Task entity = taskRepository.save(body);
            return new ResponseEntity<>(entity, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating Task: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("")
    public ResponseEntity<Task> update(@Valid @RequestBody Task body) {
        log.info("REST request to update Task : {}", body);

        try {
            Task entity = taskRepository.save(body);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating Task", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Task>> query(@QuerydslPredicate(root = Task.class) Predicate predicate, Pageable pageable) {
        log.info("REST request to get Tasks, predicate: {}, pageable: {}", predicate, pageable);

        boolean isEmptyPredicate = predicate == null ||
                (predicate instanceof BooleanBuilder && !((BooleanBuilder) predicate).hasValue());

        Page<Task> page = isEmptyPredicate
                ? taskRepository.findAll(pageable)
                : taskRepository.findAll(predicate, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));

        return ResponseEntity.ok().body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> get(@PathVariable Integer id) {
        log.info("REST request to get Task : {}", id);

        try {
            Task entity = taskRepository.findById(id).orElse(null);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting Task", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteSubject(@PathVariable Integer id) {
        log.info("REST request to delete Task : {}", id);

        try {
            taskRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error deleting Task", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
