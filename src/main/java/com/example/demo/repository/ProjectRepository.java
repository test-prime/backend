package com.example.demo.repository;

import com.example.demo.entity.Project;
import com.example.demo.entity.QProject;
import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer>, QuerydslPredicateExecutor<Project>, QuerydslBinderCustomizer<QProject> {
    /**
     * Override Querydsl handling on predicate
     */
    @Override
    default void customize(QuerydslBindings bindings, QProject root) {
        bindings.bind(root.id).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.name).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.description).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.owner.id).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.createdAt).all(ExpressionProviderFactory::getPredicate);
        bindings.bind(root.updatedAt).all(ExpressionProviderFactory::getPredicate);
    }
}
