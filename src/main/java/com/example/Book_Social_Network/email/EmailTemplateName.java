package com.example.Book_Social_Network.email;

public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account")
    ;
    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
