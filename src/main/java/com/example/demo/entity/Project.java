package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "projects")
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description = "";

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @NotNull
    private User owner;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonProperty("owner_id")
    public void setOwnerId(Integer ownerId) {
        if (ownerId != null) {
            this.owner = new User();  // Assuming User constructor or service will fetch User from ID
            this.owner.setId(ownerId); // Set owner ID manually (assuming `setId` exists on User)
        }
    }
}