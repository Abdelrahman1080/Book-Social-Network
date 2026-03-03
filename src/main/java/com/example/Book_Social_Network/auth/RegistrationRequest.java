package com.example.Book_Social_Network.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "First name must not be empty")
    @NotBlank(message = "First name must not be blank")
    private String firstName;
    @NotEmpty(message = "lastName   must not be empty")
    @NotBlank(message = "lastName   must not be blank")
    private String lastName;
    @NotEmpty(message = "email   must not be empty")
    @NotBlank(message = "email   must not be blank")
    @Email(message = "email should be well formatted")
    private String email;
    @NotEmpty(message = "password   must not be empty")
    @NotBlank(message = "password   must not be blank")
    @Size(min = 8, message = "password must be at least 8 characters long")
    private String password;

}
