package com.example.demo.repository;

import com.example.demo.entity.QUser;
import com.example.demo.entity.User;
import com.querydsl.core.types.Predicate;
import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {
    /**
     * Override Querydsl handling on predicate
     */
    @Override
    default void customize(QuerydslBindings bindings, QUser root) {
        bindings.bind(root.id).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.username).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.email).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.roles).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.createdAt).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.updatedAt).all(ExpressionProviderFactory::getPredicate);
    }
    Optional<User> findOneByUsername(String username);
}
