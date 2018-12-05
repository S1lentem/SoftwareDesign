package com.example.artem.softwaredesign.interfaces;

import android.widget.ImageView;

import com.example.artem.softwaredesign.data.models.User;

public interface OnFragmentUserEditListener {
    User getUser();
    void saveChangesFromEditing(User user);
    void comeBackFromEditing();
    void loadUserAvatar(ImageView view);
    void onPhotoUserClick();
    void exitFromEditing();
}
