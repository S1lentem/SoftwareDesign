package com.example.artem.softwaredesign.interfaces;

import android.widget.ImageView;

import com.example.artem.softwaredesign.data.models.User;

public interface OnFragmentUserInfoListener {
    void onUserEditClick();
    User getUser();
    void loadUserAvatar(ImageView view);
}
