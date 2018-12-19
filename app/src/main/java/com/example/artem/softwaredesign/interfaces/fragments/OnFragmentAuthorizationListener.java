package com.example.artem.softwaredesign.interfaces.fragments;

import com.example.artem.softwaredesign.data.exceptions.validation.EmailNotFoundException;
import com.example.artem.softwaredesign.data.exceptions.validation.PasswordDoesNotMatchException;

public interface OnFragmentAuthorizationListener {
    void onGoToRegistrationButtonClick();
    void onLogInButtonClick(String email, String password) throws PasswordDoesNotMatchException, EmailNotFoundException;
}
