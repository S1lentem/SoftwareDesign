package com.example.artem.softwaredesign.interfaces.fragments;

import com.example.artem.softwaredesign.data.exceptions.EmailAlreadyTakenException;

public interface OnFragmentRegistrationListener {
    void onRegistrationButtonClick(String firstName, String lastName, String email, String phone, String password) throws EmailAlreadyTakenException;
    void onBackButtonClick();
}
