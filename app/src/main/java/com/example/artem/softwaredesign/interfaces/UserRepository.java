package com.example.artem.softwaredesign.interfaces;

import com.example.artem.softwaredesign.data.models.User;

public interface UserRepository {
    User getUser();
    void savedUser(User user);
}
