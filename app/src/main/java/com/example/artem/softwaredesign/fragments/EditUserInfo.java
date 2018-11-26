package com.example.artem.softwaredesign.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.interfaces.UserEditListener;
import com.example.artem.softwaredesign.interfaces.UserInfoListener;

import androidx.fragment.app.Fragment;


public class EditUserInfo extends Fragment {
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;


    private UserEditListener userEditListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initializationViewComponent(inflater, container, userEditListener.getUser());


        return inflater.inflate(R.layout.fragment_edit_user_ifno, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            userEditListener = (UserEditListener) context;
        } catch (ClassCastException ex){
            throw new ClassCastException(String.format("%s must implement UserEditListener", context.toString()));
        }
    }

    private void initializationViewComponent(LayoutInflater inflater, ViewGroup container, User user) {
        View parentContainer = inflater.inflate(R.layout.fragment_edit_user_ifno, container, false);

//        firstNameEditText = parentContainer.findViewById(R.id.first_name_edit);
//        firstNameEditText.setText(user.getFirstName());
//
//        lastNameEditText = parentContainer.findViewById(R.id.last_name_edit);
//        lastNameEditText.setText(user.getLastName());
//
//        phoneEditText = parentContainer.findViewById(R.id.phone_edit);
//        phoneEditText.setText(user.getPhone());
//
//        emailEditText = parentContainer.findViewById(R.id.email_edit);
//        emailEditText.setText(user.getEmail());


        parentContainer.findViewById(R.id.back_from_edit_button)
                .setOnClickListener(v -> userEditListener.onUserEditBackClick());

        parentContainer.findViewById(R.id.back_from_edit_button)
                .setOnClickListener(v -> saveChanges());
    }

    private void saveChanges(){
        User user = new User(
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                phoneEditText.getText().toString(),
                emailEditText.getText().toString()
        );

        userEditListener.onUserEditSaveClick(user);
    }
}
