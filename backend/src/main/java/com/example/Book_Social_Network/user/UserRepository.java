package com.example.Book_Social_Network.user;

public interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<User, Integer> {
    User findByEmail(String email);
}
