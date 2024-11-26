package com.example.demo.dto;

import com.example.demo.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class Account {
    private Integer id;
    private String username;
    private String email;
    private Set<String> roles;

    public Account(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
