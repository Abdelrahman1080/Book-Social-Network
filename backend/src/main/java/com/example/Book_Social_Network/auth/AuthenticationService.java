package com.example.Book_Social_Network.auth;

import com.example.Book_Social_Network.email.EmailService;
import com.example.Book_Social_Network.email.EmailTemplateName;
import com.example.Book_Social_Network.role.RoleRepo;
import com.example.Book_Social_Network.security.JwtService;
import com.example.Book_Social_Network.user.Token;
import com.example.Book_Social_Network.user.TokenRepo;
import com.example.Book_Social_Network.user.User;
import com.example.Book_Social_Network.user.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private  final JwtService jwtService;

    @Value("${application.mailing.frontend-activation-url}")
    private String activationUrl;

    public void register(@Valid RegistrationRequest request) throws MessagingException {
        var userRole= roleRepo.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found: USER"));
        var user= User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()) )
                .roles(List.of(userRole) )
                .build();
        userRepo.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken=generateAndSaveActivationTokenForUser(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
                    );



    }

    private String generateAndSaveActivationTokenForUser(User user) {
        String generatedToken=generateActivationCode(6);
        var Token= com.example.Book_Social_Network.user.Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now()).
                expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepo.save(Token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String chars = "0123456789";
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * chars.length());
            token.append(chars.charAt(randomIndex));
        }
        return token.toString();
    }

    public AuthenticationResponse authenticate(@Valid AuthenticationRequest request) {
       var auth =authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       request.getEmail(),
                       request.getPassword()
               )
       );
       var claims =new HashMap<String,Object>();
         var user= (User) auth.getPrincipal();
            claims.put("fullname",user.getName());
         var jwtToken= jwtService.generateToken(claims,user);

         return AuthenticationResponse.builder()
               .token(jwtToken)
               .build();


    }


    public void activateAccount(String token) throws MessagingException {
        Token savedToken= tokenRepo.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid activation token"));
        if (savedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired");
        }
        User userToActivate= savedToken.getUser();
        userToActivate.setEnabled(true);
        userRepo.save(userToActivate);
        savedToken.setValidatedAt(LocalDateTime.now());
            tokenRepo.save(savedToken);


    }
}
