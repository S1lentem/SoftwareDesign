package com.example.artem.softwaredesign.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.models.RssFeed;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.data.storages.SQLite.UserImageManager;
import com.example.artem.softwaredesign.data.storages.SQLite.rss.RssSQLiteRepository;
import com.example.artem.softwaredesign.fragments.main.UserInfoFragment;
import com.example.artem.softwaredesign.interfaces.RssRepository;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentNewSourceListener;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentRssListener;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentUserEditListener;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentUserInfoListener;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends PermissionActivity
        implements OnFragmentUserInfoListener, OnFragmentUserEditListener,
        OnFragmentNewSourceListener, OnFragmentRssListener {

    private interface Navigable {
        void navigate();
    }

    private final int OPEN_CAMERA_REQUEST = 1;
    private final int OPEN_GALLERY_REQUEST = 2;

    private boolean editInfoIsCurrentFragment = false;

    private boolean isChangeAvatar = false;


    private int currentUserId;

    private UserImageManager userAvatarManager;
    private NavController navController;
    private RssRepository rssRepository;


    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAvatarManager = new UserImageManager(this);
        rssRepository = new RssSQLiteRepository(this);

        NavigationView navView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        View headerView = navView.getHeaderView(0);

        drawerLayout = findViewById(R.id.main);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(this::onToolbarNavigationClickListener);


        String userId = sessionController.getIdAuthorizedUser();
        if (userId != null) {
            currentUserId = Integer.valueOf(userId);
        } else {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);

        }

        findViewById(R.id.about_button).setOnClickListener(v -> navController.navigate(R.id.about));

        User user = userRepository.getUserById(currentUserId);
        headerView.findViewById(R.id.image_from_nav_header).setOnClickListener(v -> {
            navController.navigate(R.id.user_info_fragment);
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        TextView nameTextView = headerView.findViewById(R.id.first_name_from_nav_header);
        nameTextView.setText(user.getFirstName());

        TextView emailTextView = headerView.findViewById(R.id.email_from_nav_header);
        emailTextView.setText(user.getEmail());
    }


    @Override
    public void onBackPressed() {
        if (editInfoIsCurrentFragment) {
            User changes = checkEditingForChange();
            if (changes != null) {
                requestForSaveChanges(changes, super::onBackPressed);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public String getNewsResources() {
        return userRepository.getUserById(currentUserId).getNewsSource();
    }

    @Override
    public void redirectedToSettings() {
        navController.popBackStack();
        navController.navigate(R.id.newSourceFragment);
    }

    @Override
    public void saveRssInCache(List<RssFeed> feeds) {
        rssRepository.clearCache();
        for (RssFeed feed: feeds) {
            rssRepository.addRss(feed);
        }
    }

    @Override
    public List<RssFeed> getRssFromCache() {
        return rssRepository.getAllFeedFromCache();
    }

    @Override
    public void saveNewsResources(String resource) {
        User user = userRepository.getUserById(currentUserId);
        user.setNewsSource(resource);
        userRepository.savedUser(user);
        navController.popBackStack();
    }


    private void requestForSaveChanges(User newUser, Navigable navigatable) {
        final String positive = getResources().getString(R.string.positive_logout);
        final String negative = getResources().getString(R.string.negative_logout);
        final String title = getResources().getString(R.string.logout_description_for_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setPositiveButton(positive, (dialog, which) -> {
                    saveUserInfo(newUser, getNewAvatar());
                    navigatable.navigate();
                })
                .setNegativeButton(negative, (dialog, which) -> {
                    navigatable.navigate();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void onToolbarNavigationClickListener(View v) {
        DrawerLayout drawer = findViewById(R.id.main);

        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof NavHostFragment) {
                List<Fragment> childFragments = fragment.getChildFragmentManager().getFragments();
                if (childFragments.get(0) instanceof UserInfoFragment) {
                    toggleNavigationDrawer(drawer);
                } else {
                    if (editInfoIsCurrentFragment) {
                        User user = checkEditingForChange();
                        if (user != null) {
                            requestForSaveChanges(user, navController::popBackStack);
                        } else {
                            navController.popBackStack();
                        }
                    } else {
                        navController.popBackStack();
                    }
                }
            }
        }
    }

    private void toggleNavigationDrawer(DrawerLayout drawer) {
        int drawerLockMode = drawer.getDrawerLockMode(GravityCompat.START);
        if (drawer.isDrawerVisible(GravityCompat.START)
                && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            drawer.openDrawer(GravityCompat.START);
        }
    }


    @Override
    public void onUserEditClick() {
        navController.navigate(R.id.user_edit);
        editInfoIsCurrentFragment = true;
        isChangeAvatar = false;
    }

    @Override
    public void onLogoutClick() {
        final String positive = getResources().getString(R.string.positive_logout);
        final String negative = getResources().getString(R.string.negative_logout);
        final String title = getResources().getString(R.string.logout_description_for_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setPositiveButton(positive, (dialog, which) -> {
                    Intent intent = new Intent(this, AuthenticationActivity.class);
                    intent.putExtra(IS_LOGOUT_KEY, currentUserId);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton(negative, (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public User getUser() {
        return userRepository.getUserById(currentUserId);
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
    public void setCurrentFragmentIsEditing(boolean state) {
        this.editInfoIsCurrentFragment = state;
    }

    @Override
    public void saveChangesFromEditing(User user, Bitmap avatar) {
        saveUserInfo(user, avatar);
        navController.popBackStack();
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
                        isChangeAvatar = true;
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
                        isChangeAvatar = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void onPhotoUserClick() {
        final String camera = getResources().getString(R.string.for_camera);
        final String gallery = getResources().getString(R.string.for_gallery);

        final CharSequence[] items = {camera, gallery};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    openCamera();
                    break;
                case 1:
                    openGallery();
                    break;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, userAvatarManager.generateUriForSave().getPath());
        startActivityForResult(intent, OPEN_CAMERA_REQUEST);
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST);
    }

    private void saveUserInfo(User user, Bitmap avatar){
        userRepository.savedUser(user);
        if (isChangeAvatar) {
            userAvatarManager.updateAvatar(avatar);
        }
        updateHeader(user);
        isChangeAvatar = false;
    }

    private User checkEditingForChange() {
        TextView firstNameView = findViewById(R.id.first_name_edit);
        TextView lastNameView = findViewById(R.id.last_name_edit);
        TextView phoneView = findViewById(R.id.phone_edit);
        TextView emailView = findViewById(R.id.email_edit);

        User user = userRepository.getUserById(currentUserId);

        if (!user.getFirstName().contentEquals(firstNameView.getText()) ||
                !user.getLastName().contentEquals(lastNameView.getText()) ||
                !user.getEmail().contentEquals(emailView.getText()) ||
                !user.getPhone().contentEquals(phoneView.getText()) || isChangeAvatar) {
            return new User(
                    firstNameView.getText().toString(),
                    lastNameView.getText().toString(),
                    phoneView.getText().toString(),
                    emailView.getText().toString()
            );
        }
        return null;
    }


    private Bitmap getNewAvatar(){
        final ImageView viewForAvatar = findViewById(R.id.edit_avatar_image_view);
        return  ((BitmapDrawable) viewForAvatar.getDrawable()).getBitmap();
    }

    private void updateHeader(User user){
        NavigationView navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        loadUserAvatar(headerView.findViewById(R.id.image_from_nav_header));
        TextView nameTextView = headerView.findViewById(R.id.first_name_from_nav_header);
        nameTextView.setText(user.getFirstName());
        TextView emailTextView = headerView.findViewById(R.id.email_from_nav_header);
        emailTextView.setText(user.getEmail());
    }
}