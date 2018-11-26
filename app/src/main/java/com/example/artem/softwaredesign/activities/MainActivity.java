package com.example.artem.softwaredesign.activities;

import android.os.Bundle;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.data.storages.UserSQLiteRepository;
import com.example.artem.softwaredesign.interfaces.UserEditListener;
import com.example.artem.softwaredesign.interfaces.UserInfoListener;
import com.example.artem.softwaredesign.interfaces.UserRepository;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity
        implements UserInfoListener, UserEditListener {

    private NavController navController;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.nav_view), navController);
        NavigationUI.setupActionBarWithNavController(this, navController, findViewById(R.id.drawer_layout));


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        userRepository = new UserSQLiteRepository(this);
    }

    @Override
    public void onUserEditClick() {
        navController.navigate(R.id.edit_user_info);
    }

    @Override
    public void onUserEditSaveClick(User user) {
        userRepository.savedUser(user);
    }

    @Override
    public void onUserEditBackClick() {
        navController.popBackStack();
    }

    @Override
    public User getUser() {
        return userRepository.getUser();
    }
}
