package com.example.demo.repositories;

import com.example.demo.models.QUser;
import com.example.demo.models.User;
import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

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
        bindings.bind(root.role).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.createdAt).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.updatedAt).all(ExpressionProviderFactory::getPredicate);
    }
}
