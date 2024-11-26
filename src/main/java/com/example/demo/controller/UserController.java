package com.example.demo.controller;

import com.example.demo.dto.QueryResult;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.querydsl.core.types.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("")
    public ResponseEntity<User> create(@Valid @RequestBody User body) {
        log.info("REST request to save User : {}", body);

        try {
            User entity = userService.create(body);
            return new ResponseEntity<>(entity, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating User: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("")
    public ResponseEntity<User> update(@Valid @RequestBody User body) {
        log.info("REST request to update User : {}", body);

        try {
            User entity = userService.update(body);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating User", e);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<User>> query(HttpServletRequest request, @QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
        log.info("REST request to get Users, predicate: {}, pageable: {}", predicate, pageable);

        String queryString = request.getQueryString() != null ? request.getQueryString() : "";
        log.info("REST request to get Users, queryString: {}", queryString);
        QueryResult<User> result = userService.findAll(predicate, pageable, queryString);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotal()));

        return ResponseEntity.ok().headers(headers).body(result.getEntities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Integer id) {
        log.info("REST request to get User : {}", id);

        try {
            User entity = userService.findById(id).orElse(null);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting User", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteSubject(@PathVariable Integer id) {
        log.info("REST request to delete User : {}", id);

        try {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error deleting User", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
