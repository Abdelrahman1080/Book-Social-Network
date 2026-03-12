package com.example.Book_Social_Network.config;

import com.example.Book_Social_Network.common.PageResponce;
import com.example.Book_Social_Network.feedback.Feedback;
import com.example.Book_Social_Network.feedback.FeedbackResponce;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@OpenAPIDefinition(
        info = @Info(
                contact =  @Contact(
                        name = "Farag",
                        email = "abdelrahmanfarag499@gmail.com"
                ),
                description = "OpenAPI documentation for Book Social Network API",
                title = "Book Social Network API",
                version = "1.0",
                termsOfService = "Terms of service for Book Social Network API"
        ),
        servers= {@Server(
                description = "Local server",
                url = "http://localhost:8080/api/v1"
        )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Bearer token authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in =  SecuritySchemeIn.HEADER
)






public class OpenApiConfig {


}
