package com.example.artem.softwaredesign.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.data.storages.UserAvatarManager;
import com.example.artem.softwaredesign.interfaces.UserInfoListener;

import androidx.fragment.app.Fragment;


public class UserInfo extends Fragment{
    private ImageView avatar;
    private TextView userInfoView;


    private UserInfoListener userInfoListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentContainer = inflater.inflate(R.layout.fragment_user_info, container, false);
        userInfoView  = parentContainer.findViewById(R.id.user_info_text_view);


        User user = userInfoListener.getUser();
        if (user != null) {
            userInfoView.setText(String.format(parentContainer.getResources().getString(R.string.user_info_template),
                    user.getFirstName(), user.getLastName(), user.getPhone(), user.getEmail()));
        }



        parentContainer.findViewById(R.id.go_edit_user_info_button)
                .setOnClickListener(v -> userInfoListener.onUserEditClick());
        return parentContainer;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            userInfoListener = (UserInfoListener) context;
        } catch (ClassCastException ex){
            throw new ClassCastException(String.format("%s must implement userInfoListener", context.toString()));
        }
    }

}