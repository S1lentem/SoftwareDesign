package com.example.artem.softwaredesign.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.interfaces.OnFragmentUserEditListener;

import androidx.fragment.app.Fragment;


public class UserEditFragment extends Fragment {
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;

    private ImageView avatarView;

    private OnFragmentUserEditListener onFragmentUserEditListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentContainer = inflater.inflate(R.layout.fragment_user_edit, container, false);
        initializationViewComponent(parentContainer, onFragmentUserEditListener.getUser());


        parentContainer.findViewById(R.id.save_user_info_button)
                .setOnClickListener(v -> saveChanges());

        onFragmentUserEditListener.loadUserAvatar(avatarView);
        avatarView.setOnClickListener(v -> onFragmentUserEditListener.onPhotoUserClick());

        return parentContainer;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onFragmentUserEditListener = (OnFragmentUserEditListener) context;
        } catch (ClassCastException ex){
            throw new ClassCastException(String.format("%s must implement OnFragmentUserEditListener", context.toString()));
        }
    }


    private void initializationViewComponent(View parentContainer, User user) {
        avatarView = parentContainer.findViewById(R.id.edit_avatar_image_view);

        firstNameEditText = parentContainer.findViewById(R.id.first_name_edit);
        firstNameEditText.setText(user.getFirstName());

        lastNameEditText = parentContainer.findViewById(R.id.last_name_edit);
        lastNameEditText.setText(user.getLastName());

        phoneEditText = parentContainer.findViewById(R.id.phone_edit);
        phoneEditText.getText().insert(phoneEditText.getSelectionStart(), user.getPhone());

        emailEditText = parentContainer.findViewById(R.id.email_edit);
        emailEditText.setText(user.getEmail());
    }

    private void saveChanges(){
        User user = new User(
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                phoneEditText.getText().toString(),
                emailEditText.getText().toString()
        );
        onFragmentUserEditListener.saveChangesFromEditing(user);
    }

}
