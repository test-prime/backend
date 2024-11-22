package com.example.demo.entity;

import com.example.demo.constant.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "tasks")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @NotBlank(message = "Title is required")
    private String title;

    private String description = "";

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonProperty("project_id")
    public void setProjectId(Integer projectId) {
        if (projectId != null) {
            this.project = new Project();  // Assuming Project constructor or service will fetch Project from ID
            this.project.setId(projectId); // Set project ID manually (assuming `setId` exists on Project)
        }
    }

    @JsonProperty("assigned_to")
    public void setAssignedToId(@Nullable Integer assigned_to) {
        if (assigned_to != null) {
            this.user = new User();  // Assuming User constructor or service will fetch User from ID
            this.user.setId(assigned_to); // Set assignedTo ID manually (assuming `setId` exists on User)
        }
    }
}