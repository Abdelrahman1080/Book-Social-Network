package com.example.Book_Social_Network.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @NotEmpty(message = "email   must not be empty")
    @NotBlank(message = "email   must not be blank")
    @Email(message = "email should be well formatted")
    private String email;
    @NotEmpty(message = "password   must not be empty")
    @NotBlank(message = "password   must not be blank")
    @Size(min = 8, message = "password must be at least 8 characters long")
    private String password;

}
