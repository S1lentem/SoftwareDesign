package com.example.artem.softwaredesign.interfaces;

import com.example.artem.softwaredesign.data.models.User;

public interface UserRepository {
    void savedUser(User user);
    User getUserByEmail(String email);
    User getUserById(int id);
    void addUser(User user);
    void updateUserSettings(String feedResource, int countFeedForCache, int userId);
}
