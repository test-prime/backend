package com.example.demo.service;

import com.example.demo.dto.QueryResult;
import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaskService {
    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @CacheEvict(value = "tasks", allEntries = true)
    public Task create(Task entity) {
        log.info("Creating task: {}", entity);
        return taskRepository.save(entity);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public Task update(Task entity) {
        if (taskRepository.existsById(entity.getId())) {
            log.info("Updating task with ID: {}", entity.getId());
            return taskRepository.save(entity);
        } else {
            throw new IllegalArgumentException("Task with ID " + entity.getId() + " does not exist.");
        }
    }
    @Transactional
    @Cacheable(value = "tasks", key = "#queryString")
    public QueryResult<Task> findAll(@QuerydslPredicate(root = Task.class) Predicate predicate, Pageable pageable, String queryString) {
        log.info("Finding all tasks with predicate: {}, pageable: {}", predicate, pageable);
        boolean isEmptyPredicate = predicate == null ||
                (predicate instanceof BooleanBuilder && !((BooleanBuilder) predicate).hasValue());

        Page<Task> page = isEmptyPredicate
                ? taskRepository.findAll(pageable)
                : taskRepository.findAll(predicate, pageable);

        QueryResult<Task> result = new QueryResult<>();
        result.setTotal(page.getTotalElements());
        result.setEntities(page.getContent());

        return result; // Cache only the list of tasks
    }

    @Transactional()
    @Cacheable(value = "tasks", key = "#id")
    public Optional<Task> findById(Integer id) {
        log.info("Finding task by ID: {}", id);
        return taskRepository.findById(id);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public void deleteById(Integer id) { // Changed Long to Integer to match TaskRepository if ID is Integer
        log.info("Deleting task by ID: {}", id);
        taskRepository.deleteById(id); // Correct repository reference
    }
}
