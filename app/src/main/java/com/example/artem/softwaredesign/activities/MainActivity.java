package com.example.artem.softwaredesign.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.data.storages.SQLite.UserImageManager;
import com.example.artem.softwaredesign.data.storages.SQLite.UserSQLiteRepository;

import com.example.artem.softwaredesign.fragments.main.AboutFragment;
import com.example.artem.softwaredesign.fragments.main.NewsFragment;
import com.example.artem.softwaredesign.fragments.main.OtherFragment;
import com.example.artem.softwaredesign.fragments.main.UserEditFragment;
import com.example.artem.softwaredesign.fragments.main.UserInfoFragment;
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
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends PermissionActivity
        implements OnFragmentUserInfoListener, OnFragmentUserEditListener {

    private final int OPEN_CAMERA_REQUEST = 1;
    private final int OPEN_GALLERY_REQUEST = 2;

    private int currentUserId;

    private UserImageManager userAvatarManager;
    private NavController navController;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAvatarManager = new UserImageManager(this);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.nav_view), navController);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, findViewById(R.id.main));


        drawerLayout = findViewById(R.id.main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentUserId = bundle.getInt(CURRENT_USER_ID_KEY);
        } else {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
        }

        findViewById(R.id.about_button).setOnClickListener(v -> navController.navigate(R.id.about));

        User user = userRepository.getUserById(currentUserId);
        View headerView = navView.getHeaderView(0);
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
    public void onUserEditClick() {
        navController.navigate(R.id.user_edit);
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
    public void saveChangesFromEditing(User user) {
        final ImageView viewForAvatar = findViewById(R.id.edit_avatar_image_view);
        Bitmap avatar = ((BitmapDrawable) viewForAvatar.getDrawable()).getBitmap();
        userRepository.savedUser(user);
        userAvatarManager.updateAvatar(avatar);
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

    @Override
    public void onPhotoUserClick() {
        final String camera = getResources().getString(R.string.for_camera);
        final String gallery = getResources().getString(R.string.for_gallery);

        final CharSequence[] items = { camera, gallery };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    openCamera(null);
                    break;
                case 1:
                    openGallery();
                    break;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openCamera(ImageView view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, userAvatarManager.generateUriForSave().getPath());
        startActivityForResult(intent, OPEN_CAMERA_REQUEST);
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_GALLERY_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast toast = Toast.makeText(this, "home", Toast.LENGTH_LONG);
        toast.show();
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        StringBuilder result = new StringBuilder();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null)
                    if (fragment instanceof UserEditFragment ||
                            fragment instanceof UserInfoFragment ||
                            fragment instanceof AboutFragment ||
                            fragment instanceof OtherFragment ||
                            fragment instanceof NewsFragment) {
                        result.append("Tag: ").append(fragment.toString()).append("\n");
                    }
            }
        }
        Toast toast = Toast.makeText(this, result, Toast.LENGTH_LONG);
        toast.show();
        super.onBackPressed();
    }
}