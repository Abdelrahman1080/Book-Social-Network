package com.example.Book_Social_Network.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum BusinessErrorCodes {
    NO_CODE(0,  HttpStatus.NOT_IMPLEMENTED,"No error code"),
    INCORRECT_CURRENT_PASSWORD(300,HttpStatus.BAD_REQUEST,"Incorrect current password"),
    NEW_PASSWORD_DOES_NOT_MACH(301,HttpStatus.BAD_REQUEST,"New Password does not match"),
    ACCOUNT_LOCKED(302,HttpStatus.FORBIDDEN,"Account locked"),
    ACCOUNT_DISABLED(303,HttpStatus.FORBIDDEN,"User account is disabled"),
    BAD_CREDENTIALS(304,HttpStatus.FORBIDDEN,"Logging failed: Password or email is incorrect"),
     ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
