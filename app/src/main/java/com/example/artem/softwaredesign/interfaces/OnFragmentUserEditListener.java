package com.example.artem.softwaredesign.interfaces;

import android.widget.ImageView;

import com.example.artem.softwaredesign.data.models.User;

public interface OnFragmentUserEditListener {
    User getUser();
    void saveChangesFromEditing(User user);
    void comeBackFromEditing();
    void openCamera(ImageView view);
    void loadGallery();
    void loadUserAvatar(ImageView view);
}
