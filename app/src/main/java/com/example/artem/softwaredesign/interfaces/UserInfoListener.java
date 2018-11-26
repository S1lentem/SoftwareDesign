package com.example.artem.softwaredesign.interfaces;

import com.example.artem.softwaredesign.data.models.User;

public interface UserInfoListener {
    void onUserEditClick();
    User getUser();
}
