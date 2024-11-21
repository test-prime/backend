package com.example.demo.repositories;

import com.example.demo.models.QTask;
import com.example.demo.models.Task;
import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>, QuerydslPredicateExecutor<Task>, QuerydslBinderCustomizer<QTask> {
    /**
     * Override Querydsl handling on predicate
     */
    @Override
    default void customize(QuerydslBindings bindings, QTask root) {
        bindings.bind(root.id).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.title).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.description).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.status).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.createdAt).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.updatedAt).all(ExpressionProviderFactory::getPredicate);
    }
}
