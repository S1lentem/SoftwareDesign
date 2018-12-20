package com.example.artem.softwaredesign.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.exceptions.validation.EmptyFieldException;
import com.example.artem.softwaredesign.data.models.RssFeed;

import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.data.storages.files.UserImageManager;
import com.example.artem.softwaredesign.data.storages.SQLite.rss.RssSQLiteRepository;
import com.example.artem.softwaredesign.fragments.main.UserInfoFragment;
import com.example.artem.softwaredesign.interfaces.RssRepository;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentNewSourceListener;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentRssListener;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentUserEditListener;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentUserInfoListener;
import com.example.artem.softwaredesign.support.TextManager;
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

        String userId = sessionController.getIdAuthorizedUser();
        if (userId != null) {
            currentUserId = Integer.valueOf(userId);
        } else {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);

        }

        userAvatarManager = new UserImageManager(this, currentUserId);
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



        findViewById(R.id.about_button).setOnClickListener(this::onAboutClickListener);
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
        for (RssFeed feed: feeds.subList(0, userRepository.getUserById(currentUserId).getCountRssForCached())) {
            rssRepository.addRss(feed);
        }
    }

    @Override
    public List<RssFeed> getRssFromCache() {
        return rssRepository.getAllFeedFromCache();
    }

    @Override
    public void saveNewsResources(String resource, int count) {
        User user = userRepository.getUserById(currentUserId);
        user.setNewsSource(resource);
        user.setCountRssForCached(count);
        userRepository.savedUser(user);
        navController.popBackStack();
    }


    private void requestForSaveChanges(User newUser, Navigable navigatable) {
        final String positive = getResources().getString(R.string.positive_logout);
        final String negative = getResources().getString(R.string.negative_logout);
        final String title = getResources().getString(R.string.change_data_request);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setPositiveButton(positive, (dialog, which) -> {
                    try {
                        saveUserInfo(newUser, getNewAvatar());
                        navigatable.navigate();
                    } catch (EmptyFieldException e) {
                        Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                        e.printStackTrace();
                    }
                })
                .setNegativeButton(negative, (dialog, which) -> {
                    navigatable.navigate();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void onAboutClickListener(View v){
        if (isInMainFragment()){
            navController.navigate(R.id.about);
        } else {
            if (editInfoIsCurrentFragment){
                User user = checkEditingForChange();
                if (user != null){
                    requestForSaveChanges(user,() -> {
                        navController.popBackStack();
                        navController.navigate(R.id.about);
                    });
                }else {
                    navController.popBackStack();
                    navController.navigate(R.id.about);
                }
            } else {
                navController.popBackStack();
                navController.navigate(R.id.about);
            }
        }
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

    private boolean isInMainFragment(){
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof NavHostFragment) {
                List<Fragment> childFragments = fragment.getChildFragmentManager().getFragments();
                if (childFragments.get(0) instanceof UserInfoFragment) {
                    return true;
                }
            }
        }
        return false;
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
                    rssRepository.clearCache();
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
            view.setRotation(-90);
        }
    }

    @Override
    public void setCurrentFragmentIsEditing(boolean state) {
        this.editInfoIsCurrentFragment = state;
    }

    @Override
    public void saveChangesFromEditing(User user, Bitmap avatar) throws EmptyFieldException {
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
                        viewForAvatar.setRotation(-90);
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
                        viewForAvatar.setRotation(-90);
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

    private void saveUserInfo(User user, Bitmap avatar) throws EmptyFieldException {
       if (TextManager.isEmpty(user.getFirstName(), user.getLastName(),
                user.getPhone(), user.getEmail())) {
            throw new EmptyFieldException(getResources().getString(R.string.message_for_empty_fields));
        }
        userRepository.savedUser(user);
        if (isChangeAvatar) {
            userAvatarManager.updateAvatar(avatar);
        }
        updateHeader(user);
        isChangeAvatar = false;
    }

    private User checkEditingForChange()  {
        String firstName = ((EditText)findViewById(R.id.first_name_edit)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.last_name_edit)).getText().toString();
        String phone = ((EditText)findViewById(R.id.phone_edit)).getText().toString();
        String email = ((EditText)findViewById(R.id.email_edit)).getText().toString();

        User user = userRepository.getUserById(currentUserId);
        if (!user.getFirstName().equals(firstName) || !user.getLastName().equals(lastName) ||
                !user.getEmail().equals(email) || !user.getPhone().equals(phone) || isChangeAvatar) {
            return new User(firstName, lastName, phone, email);
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