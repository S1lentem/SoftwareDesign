package com.example.artem.softwaredesign.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.data.storages.UserAvatarManager;
import com.example.artem.softwaredesign.data.storages.UserSQLiteRepository;
import com.example.artem.softwaredesign.interfaces.UserEditListener;
import com.example.artem.softwaredesign.interfaces.UserInfoListener;
import com.example.artem.softwaredesign.interfaces.UserRepository;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity
        implements UserInfoListener, UserEditListener {

    private final int TAKE_PICTURE_REQUEST = 1;
    private final int GALLERY_REQUEST = 2;

    private UserAvatarManager userAvatarManager;

    private NavController navController;
    private UserRepository userRepository;

    private ImageView viewForAvatar;
    private Uri photoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.nav_view), navController);
        NavigationUI.setupActionBarWithNavController(this, navController, findViewById(R.id.drawer_layout));


        viewForAvatar = findViewById(R.id.edit_avatar_image_view);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        userRepository = new UserSQLiteRepository(this);
        userAvatarManager = new UserAvatarManager(this);
    }

    @Override
    public void onUserEditClick() {
        navController.navigate(R.id.user_edit);
    }

    @Override
    public User getUser() {
        return userRepository.getUser();
    }

    @Override
    public void saveChangesFromEditing(User user) {
        userRepository.savedUser(user);
        userAvatarManager.updateAvatar(this);
        navController.popBackStack();
    }

    @Override
    public void comeBackFromEditing() {
        navController.popBackStack();
    }

    @Override
    public void createNewAvatar(ImageView view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = userAvatarManager.createFileImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = Uri.fromFile(photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, TAKE_PICTURE_REQUEST);
        }
    }

    @Override
    public void loadNewAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PICTURE_REQUEST:
                if (resultCode == RESULT_OK && data != null){
                    
                }
                break;
            case GALLERY_REQUEST:
                break;
        }
    }
}