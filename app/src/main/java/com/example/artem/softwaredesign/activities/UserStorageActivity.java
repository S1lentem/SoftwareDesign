package com.example.artem.softwaredesign.activities;

import android.os.Bundle;

import com.example.artem.softwaredesign.data.storages.SQLite.UserSQLiteRepository;
import com.example.artem.softwaredesign.data.user.SessionController;
import com.example.artem.softwaredesign.interfaces.UserRepository;

import androidx.appcompat.app.AppCompatActivity;

public class UserStorageActivity extends AppCompatActivity {

    protected final String IS_LOGOUT_KEY = "is_logout";
    protected UserRepository userRepository;
    protected SessionController sessionController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionController = new SessionController(this);
        userRepository = new UserSQLiteRepository(this);
    }
}
