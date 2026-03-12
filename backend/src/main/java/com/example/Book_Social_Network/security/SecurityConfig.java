package com.example.Book_Social_Network.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtAuthFilter JwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.cors(Customizer.withDefaults())
                    // تعطيل CSRF لأننا ستايتلس
                    .csrf(csrf -> csrf.disable())

                    // الصلاحيات

                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/auth/**",
                                    "/v2/api-docs",
                                    "/v3/api-docs",
                                    "/swagger-resources/**",
                                    "/swagger-resources",
                                    "/configuration/ui",
                                    "/configuration/security",
                                    "/swagger-ui/**",
                                    "/webjars/**",
                                    "/swagger-ui.html",
                                    "/swagger-ui/index.html#/**",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html"


                            ).permitAll()
                            .anyRequest().authenticated()
                    )



                    // Session Stateless
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )

                    // Authentication provider
                    .authenticationProvider(authenticationProvider)

                    // فلتر JWT قبل UsernamePasswordAuthenticationFilter
                    .addFilterBefore(JwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
