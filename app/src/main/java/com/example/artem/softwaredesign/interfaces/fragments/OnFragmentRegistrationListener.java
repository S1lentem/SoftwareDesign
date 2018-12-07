package com.example.artem.softwaredesign.interfaces.fragments;

public interface OnFragmentRegistrationListener {
    void onRegistrationButtonClick(String firstName, String lastName, String email, String phone, String password);
    void onBackButtonClick();
}
