package com.example.artem.softwaredesign.source;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.storages.UserDbHelper;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.data.storages.UserSQLiteRepository;

import androidx.fragment.app.Fragment;


public class UserInfo extends Fragment {
    private ImageView avatar;
    private TextView userInfoView;

    private UserSQLiteRepository userRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentContainer = inflater.inflate(R.layout.fragment_user_info, container, false);
        userInfoView  = parentContainer.findViewById(R.id.user_info_text_view);

        User user = userRepository.getUser();
        if (user != null) {
            userInfoView.setText(String.format(parentContainer.getResources().getString(R.string.user_info_template),
                    user.getFirstName(), user.getLastName(), user.getPhone(), user.getEmail()));
        }
        return parentContainer;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.userRepository = new UserSQLiteRepository(context);
    }
}