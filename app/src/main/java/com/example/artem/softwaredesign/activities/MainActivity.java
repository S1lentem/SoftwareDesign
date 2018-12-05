package com.example.artem.softwaredesign.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.data.storages.UserImageManager;
import com.example.artem.softwaredesign.data.storages.UserSQLiteRepository;
import com.example.artem.softwaredesign.interfaces.OnFragmentUserEditListener;
import com.example.artem.softwaredesign.interfaces.OnFragmentUserInfoListener;
import com.example.artem.softwaredesign.interfaces.UserRepository;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends PermissionActivity
        implements OnFragmentUserInfoListener, OnFragmentUserEditListener {

    private final int OPEN_CAMERA_REQUEST = 1;
    private final int OPEN_GALLERY_REQUEST = 2;

    private UserImageManager userAvatarManager;

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
        NavigationUI.setupActionBarWithNavController(this, navController, findViewById(R.id.main));


        DrawerLayout drawer = findViewById(R.id.main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        userRepository = new UserSQLiteRepository(this);
        userAvatarManager = new UserImageManager(this);
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
    public void loadUserAvatar(ImageView view) {
        Uri uriForAvatar = userAvatarManager.getUriForUserAvatar();
        if (uriForAvatar != null) {
            view.setImageURI(uriForAvatar);
            view.setRotation(90);
        }
    }

    @Override
    public void saveChangesFromEditing(User user) {
        final ImageView viewForAvatar = findViewById(R.id.edit_avatar_image_view);
        Bitmap avatar = ((BitmapDrawable) viewForAvatar.getDrawable()).getBitmap();
        userRepository.savedUser(user);
        userAvatarManager.updateAvatar(avatar);
        navController.popBackStack();
    }

    @Override
    public void comeBackFromEditing() {
        navController.popBackStack();
    }

    @Override
    public void openCamera(ImageView view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, userAvatarManager.generateUriForSave().getPath());
        startActivityForResult(intent, OPEN_CAMERA_REQUEST);
    }

    @Override
    public void loadGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ImageView viewForAvatar = findViewById(R.id.edit_avatar_image_view);
        Bitmap avatar;
        switch (requestCode) {
            case OPEN_CAMERA_REQUEST:
                if (resultCode == RESULT_OK && data != null) {
                    if (viewForAvatar != null) {
                        avatar = (Bitmap) data.getExtras().get("data");
                        viewForAvatar.setImageBitmap(avatar);
                        viewForAvatar.setRotation(90);
                    }
                }
                break;
            case OPEN_GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        avatar = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        viewForAvatar.setImageBitmap(avatar);
                        viewForAvatar.setRotation(90);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}