package com.example.artem.softwaredesign.data.storages;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class UserAvatarManager {

    private final String avatarFileName = "avatar.jpg";
    private final String tempAvatarFileName = "temp";

    private Context context;

    public UserAvatarManager(Context context){
        this.context = context;
    }

    public void updateAvatar(Context context){
        File newAvatar = new File(context.getCacheDir().getAbsoluteFile() + File.separator
                + tempAvatarFileName);
        if (newAvatar.exists()){
            newAvatar.renameTo(new File(context.getCacheDir().getAbsoluteFile() + File.separator
                    + avatarFileName));
        }
    }

    public String getUriForTempAvatar(boolean autoCreated){
        File avatar = new File(context.getCacheDir().getAbsolutePath(), tempAvatarFileName);
        if (autoCreated){
            try {
                avatar.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return avatar.getAbsolutePath();
    }

    public File createFileImage() throws IOException {
        File imageFile = File.createTempFile(tempAvatarFileName,  ".jpg", context.getCacheDir());
        return imageFile;
    }
}
