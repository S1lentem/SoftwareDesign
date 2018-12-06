package com.example.artem.softwaredesign.interfaces.fragments;

import com.example.artem.softwaredesign.data.models.User;

public interface OnFragmentRegistrationListener {
    void onRegistrationButtonClick(User user);
    void onBackButtonClick();
}
