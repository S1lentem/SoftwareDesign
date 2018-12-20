package com.example.artem.softwaredesign.interfaces.fragments;

import com.example.artem.softwaredesign.data.models.User;

public interface OnFragmentNewSourceListener {
    void saveNewsResources(String resource, int count);
    User getUser();
}
