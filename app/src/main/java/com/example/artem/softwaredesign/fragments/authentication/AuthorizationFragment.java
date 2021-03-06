package com.example.artem.softwaredesign.fragments.authentication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.exceptions.EmailNotFoundException;
import com.example.artem.softwaredesign.data.exceptions.PasswordDoesNotMatchException;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentAuthorizationListener;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;

public class AuthorizationFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;

    private OnFragmentAuthorizationListener onFragmentAuthorizationListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);
        emailEditText = view.findViewById(R.id.email_for_authorization);
        passwordEditText = view.findViewById(R.id.password_for_authorization);
        emailTextInputLayout = view.findViewById(R.id.email_for_authorization_text_input_layout);
        passwordTextInputLayout = view.findViewById(R.id.password_for_authorization_text_input_layout);

        view.findViewById(R.id.log_in_button).setOnClickListener(
                v -> onLogInButtonClick());

        view.findViewById(R.id.go_to_registration_button).setOnClickListener(
                v -> onFragmentAuthorizationListener.onGoToRegistrationButtonClick());

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentAuthorizationListener) {
            onFragmentAuthorizationListener = (OnFragmentAuthorizationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentAuthorizationListener");
        }
    }

    private void onLogInButtonClick(){
        if (isValidForm()){
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            try {
                onFragmentAuthorizationListener.onLogInButtonClick(email, password);
            } catch (PasswordDoesNotMatchException e) {
                passwordTextInputLayout.setError(getResources().getString(R.string.message_for_missing_password));
            } catch (EmailNotFoundException e) {
                emailTextInputLayout.setError(getResources().getString(R.string.message_for_email_not_found));
            }
        }
    }

    private boolean isValidForm(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.isEmpty()){
            emailTextInputLayout.setError(getResources().getString(R.string.message_for_empty_email));
        }
        if (password.isEmpty()){
            passwordTextInputLayout.setError(getResources().getString(R.string.message_for_empty_password));
        }
        return  !password.isEmpty() && !email.isEmpty();
    }
}
