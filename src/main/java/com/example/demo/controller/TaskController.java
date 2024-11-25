
package com.example.demo.controller;

import com.example.demo.dto.QueryResult;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.repository.taskService;
import com.example.demo.service.TaskService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TaskService taskService;

    @PostMapping("")
    public ResponseEntity<Task> create(@Valid @RequestBody Task body) {
        log.info("REST request to save Task : {}", body);

        try {
            Task entity = taskService.create(body);
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
            Task entity = taskService.update(body);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating Task", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Task>> query(HttpServletRequest request, @QuerydslPredicate(root = Task.class) Predicate predicate, Pageable pageable) {
        log.info("REST request to get Tasks, predicate: {}, pageable: {}", predicate, pageable);

        String queryString = request.getQueryString();
        QueryResult<Task> result = taskService.findAll(predicate, pageable, queryString);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotal()));

        return ResponseEntity.ok().body(result.getEntities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> get(@PathVariable Integer id) {
        log.info("REST request to get Task : {}", id);

        try {
            Task entity = taskService.findById(id).orElse(null);
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
            taskService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error deleting Task", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
