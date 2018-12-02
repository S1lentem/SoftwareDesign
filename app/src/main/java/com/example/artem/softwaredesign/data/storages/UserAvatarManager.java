package com.example.artem.softwaredesign.data.storages;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserAvatarManager {

    private final String AVATAR_NAME = "avatar.jpg";
    private Context context;

    public UserAvatarManager(Context context){
        this.context = context;
    }

    public void updateAvatar(Bitmap avatar){
        File newAvatar = new File(context.getCacheDir().getAbsolutePath(), AVATAR_NAME);
        try {
            FileOutputStream out = new FileOutputStream(newAvatar);
            avatar.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Uri getUriForUserAvatar(){
        File file = new File(context.getCacheDir().getAbsolutePath(), AVATAR_NAME);
        return file.exists() ? Uri.fromFile(file) : null;
    }

    public Uri generateUriForSave(){
        File file = new File(context.getCacheDir().getAbsolutePath(), AVATAR_NAME);
        return Uri.fromFile(file);
    }
}
