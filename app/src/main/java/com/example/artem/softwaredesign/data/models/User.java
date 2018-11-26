package com.example.artem.softwaredesign.data.models;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public User(String firsName, String lastName, String email, String phone){
        this.firstName = firsName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone(){
        return this.phone;
    }

}
