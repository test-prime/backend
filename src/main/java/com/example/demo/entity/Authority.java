package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;


import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "authorities")
public class Authority implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String name;
}