package com.example.demo.service;

import com.example.demo.dto.QueryResult;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
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

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @CacheEvict(value = "users", allEntries = true)
    public User create(User entity) {
        log.info("Creating user: {}", entity);
        return userRepository.save(entity);
    }

    @CachePut(value = "users", key = "#entity.id")
    public User update(User entity) {
        if (userRepository.existsById(entity.getId())) {
            log.info("Updating user with ID: {}", entity.getId());
            return userRepository.save(entity);
        } else {
            throw new IllegalArgumentException("User with ID " + entity.getId() + " does not exist.");
        }
    }

    @Cacheable(value = "users", key = "'all_users_' + #predicate.toString() + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()")
    public QueryResult<User> findAll(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
        log.info("Finding all users with predicate: {}, pageable: {}", predicate, pageable);
        boolean isEmptyPredicate = predicate == null ||
                (predicate instanceof BooleanBuilder && !((BooleanBuilder) predicate).hasValue());

        Page<User> page = isEmptyPredicate
                ? userRepository.findAll(pageable)
                : userRepository.findAll(predicate, pageable);

        QueryResult<User> result = new QueryResult<>();
        result.setTotal(page.getTotalElements());
        result.setEntities(page.getContent());

        return result; // Cache only the list of users
    }

    @Cacheable(value = "users", key = "#id")
    public Optional<User> findById(Integer id) {
        log.info("Finding user by ID: {}", id);
        return userRepository.findById(id);
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteById(Integer id) { // Changed Long to Integer to match UserRepository if ID is Integer
        log.info("Deleting user by ID: {}", id);
        userRepository.deleteById(id); // Correct repository reference
    }
}
