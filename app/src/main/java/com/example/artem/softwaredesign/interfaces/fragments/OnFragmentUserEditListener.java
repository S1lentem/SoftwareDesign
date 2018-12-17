package com.example.artem.softwaredesign.interfaces.fragments;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.artem.softwaredesign.data.models.User;

public interface OnFragmentUserEditListener {
    User getUser();
    void saveChangesFromEditing(User user, Bitmap avatar);
    void loadUserAvatar(ImageView view);
    void onPhotoUserClick();
    void setCurrentFragmentIsEditing(boolean state);
}
