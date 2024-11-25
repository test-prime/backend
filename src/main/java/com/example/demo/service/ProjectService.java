package com.example.demo.service;

import com.example.demo.controller.ProjectController;
import com.example.demo.dto.QueryResult;
import com.example.demo.entity.Project;
import com.example.demo.repository.ProjectRepository;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final Logger log = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectRepository projectRepository;

    @CacheEvict(value = "projects", allEntries = true)
    public Project create(Project entity) {
        log.info("Creating project: {}", entity);
        return projectRepository.save(entity);
    }

    @CachePut(value = "projects", key = "#entity.id")
    public Project update(Project entity) {
        if (projectRepository.existsById(entity.getId())) {
            log.info("Updating project with ID: {}", entity.getId());
            return projectRepository.save(entity);
        } else {
            throw new IllegalArgumentException("Project with ID " + entity.getId() + " does not exist.");
        }
    }

    @Transactional()
    @Cacheable(value = "projects", key = "#queryString")
    public QueryResult<Project> findAll(@QuerydslPredicate(root = Project.class) Predicate predicate, Pageable pageable, String queryString) {
        log.info("Finding all projects with predicate: {}, pageable: {}", predicate, pageable);

        boolean isEmptyPredicate = predicate == null ||
                (predicate instanceof BooleanBuilder && !((BooleanBuilder) predicate).hasValue());

        Page<Project> page = isEmptyPredicate
                ? projectRepository.findAll(pageable)
                : projectRepository.findAll(predicate, pageable);

        QueryResult<Project> result = new QueryResult<>();

        result.setTotal(page.getTotalElements());
        result.setEntities(page.getContent());

        return result; // Cache only the list of projects
    }

    @Cacheable(value = "projects", key = "#id")
    public Optional<Project> findById(Integer id) {
        log.info("Finding project by ID: {}", id);
        return projectRepository.findById(id);
    }

    @CacheEvict(value = "projects", allEntries = true)
    public void deleteById(Integer id) { // Changed Long to Integer to match ProjectRepository if ID is Integer
        log.info("Deleting project by ID: {}", id);
        projectRepository.deleteById(id); // Correct repository reference
    }
}
