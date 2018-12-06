package com.example.artem.softwaredesign.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.exceptions.EmailNotFoundException;
import com.example.artem.softwaredesign.data.exceptions.PasswordDoesNotMatchException;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentAuthorizationListener;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentRegistrtionListener;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class AuthenticationActivity extends UserStorageActivity
    implements OnFragmentAuthorizationListener, OnFragmentRegistrtionListener {

    private NavController navController;

    private final String USER_KEY = "user_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(IS_LOGOUT_KEY)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(CURRENT_USER_ID_KEY);
            editor.apply();
        }


        if (sharedPreferences.contains(CURRENT_USER_ID_KEY)){
            String id = sharedPreferences.getString(CURRENT_USER_ID_KEY, null);
            if (id != null) {
                logIn(Integer.parseInt(id));
            }
        }

        setContentView(R.layout.activity_authentication);
        navController = Navigation.findNavController(this, R.id.auth_host_fragment);
    }

    @Override
    public void onRegistrationButtonClick(){
        navController.navigate(R.id.registrationFragment);
    }

    @Override
    public void onLogInButtonClick(String email, String password)
            throws PasswordDoesNotMatchException, EmailNotFoundException {
        User user = userRepository.getUserByEmail(email);
        String message = "ok";
        if (user != null){
            if (user.getPassword().equals(password)){
                logIn(user.getId());
            }
            else {
                throw new PasswordDoesNotMatchException();
            }
        }
        else {
            throw new EmailNotFoundException();
        }
        Toast tost = Toast.makeText(this, message, Toast.LENGTH_LONG);
        tost.show();
    }

    @Override
    public void onRegistrationUserClick(User user) {
        userRepository.addUser(user);
    }


    public void logIn(int id){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CURRENT_USER_ID_KEY, String.valueOf(id));
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(CURRENT_USER_ID_KEY, id);
        startActivity(intent);
    }
}
