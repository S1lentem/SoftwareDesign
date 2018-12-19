package com.example.artem.softwaredesign.fragments.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.exceptions.validation.EmailAlreadyTakenException;
import com.example.artem.softwaredesign.data.exceptions.validation.EmptyFieldException;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentUserEditListener;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;


public class UserEditFragment extends Fragment {
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;

    private ImageView avatarView;
    private TextInputLayout emaileTextInputLayout;

    private OnFragmentUserEditListener onFragmentUserEditListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentContainer = inflater.inflate(R.layout.fragment_user_edit, container, false);
        initializationViewComponent(parentContainer, onFragmentUserEditListener.getUser());


        parentContainer.findViewById(R.id.save_user_info_button)
                .setOnClickListener(v -> saveChanges());



        onFragmentUserEditListener.loadUserAvatar(avatarView);
        avatarView.setOnClickListener(v -> onFragmentUserEditListener.onPhotoUserClick());


        if(savedInstanceState != null) {
            Bitmap bitmap = savedInstanceState.getParcelable("image");
            avatarView.setImageBitmap(bitmap);
        }

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
        phoneEditText.setText(user.getPhone());

        emaileTextInputLayout = parentContainer.findViewById(R.id.edit_email_text_input_layout);
        emailEditText = parentContainer.findViewById(R.id.email_edit);
        emailEditText.setText(user.getEmail());
    }

    @Override
    public void onResume() {
        onFragmentUserEditListener.setCurrentFragmentIsEditing(true);

        super.onResume();
    }

    private User getUserFromEditForm(){
        User user = onFragmentUserEditListener.getUser();
        return new User(
                user.getId(),
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                emailEditText.getText().toString(),
                phoneEditText.getText().toString(),
                user.getPassword()
        );
    }

    private void saveChanges(){
        Bitmap avatar = ((BitmapDrawable) avatarView.getDrawable()).getBitmap();
        try {
            onFragmentUserEditListener.saveChangesFromEditing(getUserFromEditForm(), avatar);
        } catch (EmailAlreadyTakenException ex){
            emaileTextInputLayout.setError(String.format(
                    getResources().getString(R.string.message_for_already_email), ex.getEmail()));
        } catch (EmptyFieldException e) {
            Toast toast = Toast.makeText((Context)onFragmentUserEditListener, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onPause() {
        onFragmentUserEditListener.setCurrentFragmentIsEditing(false);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        BitmapDrawable drawable = (BitmapDrawable) avatarView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        outState.putParcelable("image", bitmap);
        super.onSaveInstanceState(outState);
    }
}
