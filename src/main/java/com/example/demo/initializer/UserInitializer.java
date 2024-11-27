package com.example.demo.initializer;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@localhost")
                    .password("$2a$10$EMFg7cP5V01fR6n3AgUA4exnkTkSTXUaDvIyHxfvD4MRjYOw1rTti") // Nên mã hóa mật khẩu
                    .roles(Set.of("ADMIN")) // Gán các roles
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
            System.out.println("Admin user initialized with roles: {}" + admin.getRoles());
        }
    }
}
