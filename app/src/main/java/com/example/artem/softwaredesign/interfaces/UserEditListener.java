package com.example.artem.softwaredesign.interfaces;

import com.example.artem.softwaredesign.data.models.User;

public interface UserEditListener {
    void onUserEditSaveClick(User user);
    void onUserEditBackClick();
    User getUser();
}
