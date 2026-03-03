package com.example.Book_Social_Network.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().contains("/api/v1/auth")){
            filterChain.doFilter(request, response);
            return;
        }
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
               filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail=jwtService.extractUsername(jwt);
            if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                 UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if(jwtService.validateToken(jwt,userDetails)){
                     UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                     authentication.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
            }
                 


    }
        filterChain.doFilter(request, response);
}
}
