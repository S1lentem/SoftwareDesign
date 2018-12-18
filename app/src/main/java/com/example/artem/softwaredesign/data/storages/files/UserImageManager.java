package com.example.artem.softwaredesign.data.storages.files;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserImageManager {

    private final String fileName;
    private Context context;

    public UserImageManager(Context context, int userId){
        fileName = String.valueOf(userId) + ".jpg";
        this.context = context;
    }

    public void updateAvatar(Bitmap avatar){
        File newAvatar = new File(context.getCacheDir().getAbsolutePath(), fileName);
        try {
            FileOutputStream out = new FileOutputStream(newAvatar);
            avatar.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Uri getUriForUserAvatar(){
        File file = new File(context.getCacheDir().getAbsolutePath(), fileName);
        return file.exists() ? Uri.fromFile(file) : null;
    }

    public Uri generateUriForSave(){
        File file = new File(context.getCacheDir().getAbsolutePath(), fileName);
        return Uri.fromFile(file);
    }
}
