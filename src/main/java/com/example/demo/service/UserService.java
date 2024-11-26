package com.example.demo.service;

import com.example.demo.dto.QueryResult;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Security;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @CacheEvict(value = "users", allEntries = true)
    public User create(User entity) {
        log.info("Creating user: {}", entity);
        String encryptedPassword = passwordEncoder.encode(entity.getPassword());
        entity.setPassword(encryptedPassword);
        return userRepository.save(entity);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User update(User entity) {
        if (userRepository.existsById(entity.getId())) {
            log.info("Updating user with ID: {}", entity.getId());
            userRepository.findById(entity.getId()).ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!Objects.equals(entity.getPassword(), currentEncryptedPassword)) {
                    String encryptedPassword = passwordEncoder.encode(entity.getPassword());
                    entity.setPassword(encryptedPassword);
                }
            });
            return userRepository.save(entity);
        } else {
            throw new IllegalArgumentException("User with ID " + entity.getId() + " does not exist.");
        }
    }

    @Transactional
    @Cacheable(value = "users", key = "#queryString")
    public QueryResult<User> findAll(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable, String queryString) {
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

    @Transactional()
    @Cacheable(value = "users", key = "#id")
    public Optional<User> findById(Integer id) {
        log.info("Finding user by ID: {}", id);
        return userRepository.findById(id);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteById(Integer id) { // Changed Long to Integer to match UserRepository if ID is Integer
        log.info("Deleting user by ID: {}", id);
        userRepository.deleteById(id); // Correct repository reference
    }

    public Optional<User> getUserWithRoles() {
        return Security.getCurrentUserLogin().flatMap(userRepository::findOneByUsername);
    }
}
