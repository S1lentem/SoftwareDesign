package com.example.artem.softwaredesign.data.exceptions.validation;

public class EmailAlreadyTakenException extends Exception {
    private String email;
    public  EmailAlreadyTakenException(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
