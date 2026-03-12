package com.example.Book_Social_Network.auth;


import com.example.Book_Social_Network.user.UserRepo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name="Authentication", description = "Endpoints for user authentication and registration")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepo userRepo;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerUser(@Valid @RequestBody  RegistrationRequest request) {
        try {
            authenticationService.register(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping ("/authenticate")
    public ResponseEntity<AuthenticationResponse> loginUser(@Valid @RequestBody AuthenticationRequest request) {
            var response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/activate-account")
    public void activateAccount(@RequestParam("token") String token) throws MessagingException {
        authenticationService.activateAccount(token);
    }



    @GetMapping("/getallusers")
    public ResponseEntity<?> getAllUsers() {
        try {
            var users = userRepo.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to retrieve users: " + e.getMessage());
        }
    }

}
