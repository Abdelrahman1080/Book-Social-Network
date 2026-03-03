package com.example.Book_Social_Network;

import com.example.Book_Social_Network.role.Role;
import com.example.Book_Social_Network.role.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class BookSocialNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookSocialNetworkApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(RoleRepo roleRepo) {
        return args -> {
            if (roleRepo.findByName("USER").isEmpty()) {
                roleRepo.save(Role.builder().creationDate(LocalDate.now()).name("USER").build());


            }
        };

    }
}
