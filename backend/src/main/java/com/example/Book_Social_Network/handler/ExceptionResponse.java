package com.example.Book_Social_Network.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {
    private  Integer bussinessErrorCode;
    private  String bussinessErrorDescription;
    private  String error;
    private Set<String> validationErrors;
    private Map<String,String> errors;

}
