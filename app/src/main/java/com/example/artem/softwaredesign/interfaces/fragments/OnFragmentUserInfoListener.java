package com.example.artem.softwaredesign.interfaces.fragments;

import android.widget.ImageView;

import com.example.artem.softwaredesign.data.models.User;

public interface OnFragmentUserInfoListener {
    void onUserEditClick();
    void onLogoutClick();
    User getUser();
    void loadUserAvatar(ImageView view);
}
