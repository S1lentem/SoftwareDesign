package com.example.artem.softwaredesign.data.user;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.Context.MODE_PRIVATE;

public class SessionController {

    private final String CURRENT_USER_ID_KEY = "current_user";
    private final String PREFERENCES_NAME = "user_controller";

    private SharedPreferences preferences;


    public SessionController(AppCompatActivity context) {
        this.preferences = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
    }

    public String getIdAuthorizedUser() {
        return preferences.contains(CURRENT_USER_ID_KEY)
                ? preferences.getString(CURRENT_USER_ID_KEY, null) : null;

    }

    public void logIn(String id) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CURRENT_USER_ID_KEY, String.valueOf(id));
        editor.apply();
    }

    public void logOut() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(CURRENT_USER_ID_KEY);
        editor.apply();
    }
}
