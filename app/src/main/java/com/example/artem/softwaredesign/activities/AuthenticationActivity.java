package com.example.artem.softwaredesign.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.crypto.EncryptionAlgorithm;
import com.example.artem.softwaredesign.data.crypto.HashManager;
import com.example.artem.softwaredesign.data.exceptions.about.NotAccessToImeiException;
import com.example.artem.softwaredesign.data.exceptions.validation.EmailAlreadyTakenException;
import com.example.artem.softwaredesign.data.exceptions.validation.EmailNotFoundException;
import com.example.artem.softwaredesign.data.exceptions.validation.PasswordDoesNotMatchException;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentAboutListener;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentAuthorizationListener;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentRegistrationListener;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class AuthenticationActivity extends PermissionActivity
    implements OnFragmentAuthorizationListener, OnFragmentRegistrationListener {

    private NavController navController;
    private int DEFAULT_COUNT_RSS_NEWS_FOR_SAVE_CACHE = 10;

    private final HashManager hashManager = new HashManager(EncryptionAlgorithm.MD5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(IS_LOGOUT_KEY)){
            sessionController.logOut();
        }

        String userId = sessionController.getIdAuthorizedUser();
        String validPassword = userRepository.getUserById(Integer.parseInt(userId)).getPassword();
        String authorizedPassword = sessionController.getPasswordHashAuthorizedUser();

        if (userId != null && validPassword.equals(authorizedPassword)){
            logIn(userId, validPassword);
        }

        setContentView(R.layout.activity_authentication);
        navController = Navigation.findNavController(this, R.id.auth_host_fragment);

    }

    @Override
    public void onGoToRegistrationButtonClick(){
        navController.navigate(R.id.registrationFragment);
    }

    @Override
    public void goToAbout(){
        navController.navigate(R.id.aboutFragment);
    }

    @Override
    public void onLogInButtonClick(String email, String password)
            throws PasswordDoesNotMatchException, EmailNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user != null){
            if (user.getPassword().equals(hashManager.getHash(password))){
                logIn(String.valueOf(user.getId()), user.getPassword());
            }
            else {
                throw new PasswordDoesNotMatchException();
            }
        }
        else {
            throw new EmailNotFoundException();
        }
    }

    public void logIn(String id, String password){
        sessionController.logIn(id, password);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRegistrationButtonClick(String firstName, String lastName, String email,
                                          String phone, String password) throws EmailAlreadyTakenException {
        if (userRepository.getUserByEmail(email) != null){
            throw new EmailAlreadyTakenException(email);
        }
        User user = new User(0, firstName, lastName, email, phone,
                hashManager.getHash(password), null, DEFAULT_COUNT_RSS_NEWS_FOR_SAVE_CACHE);
        userRepository.addUser(user);
        int id = userRepository.getUserByEmail(user.getEmail()).getId();
        logIn(String.valueOf(id), password);
    }
}
