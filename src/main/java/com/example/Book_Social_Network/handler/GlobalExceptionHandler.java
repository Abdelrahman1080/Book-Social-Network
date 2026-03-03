package com.example.Book_Social_Network.handler;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.Book_Social_Network.handler.BusinessErrorCodes.*;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .bussinessErrorCode(ACCOUNT_LOCKED.getCode())
                                .bussinessErrorDescription(ACCOUNT_LOCKED.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
        }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .bussinessErrorCode(ACCOUNT_DISABLED.getCode())
                                .bussinessErrorDescription(ACCOUNT_DISABLED.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exp) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .bussinessErrorCode(BAD_CREDENTIALS.getCode())
                                .bussinessErrorDescription(BAD_CREDENTIALS.getDescription())
                                .error(BAD_CREDENTIALS.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exp) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp) {
       Set<String> errors = exp.getBindingResult().getFieldErrors().stream()
               .map(error -> error.getField() + ": " + error.getDefaultMessage())
               .collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)

                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
        exp.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .bussinessErrorDescription("An unexpected error occurred")
                                .error(exp.getMessage())
                                .build()
                );
    }


}
