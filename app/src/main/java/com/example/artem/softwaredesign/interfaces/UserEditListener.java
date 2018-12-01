package com.example.artem.softwaredesign.interfaces;

import android.widget.ImageView;

import com.example.artem.softwaredesign.data.models.User;

public interface UserEditListener {
    User getUser();
    void saveChangesFromEditing(User user);
    void comeBackFromEditing();
    void createNewAvatar(ImageView view);
    void loadNewAvatar();
}
