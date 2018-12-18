package com.example.artem.softwaredesign.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.example.artem.softwaredesign.BuildConfig;
import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.exceptions.about.NotAccessToImeiException;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentAboutListener;

import androidx.core.app.ActivityCompat;

public class PermissionActivity extends UserStorageActivity
        implements OnFragmentAboutListener {

    private final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getVersion() {
        return BuildConfig.VERSION_NAME;
    }


    @Override
    public String getImei() throws NotAccessToImeiException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            return manager.getDeviceId();
        }
        throw new NotAccessToImeiException();
    }

    @Override
    public void requestPermissionForImei() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                            REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
    }

    @Override
    public String getDescriptionPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
                return getResources().getString(R.string.permission_description);
            }
            return getResources().getString(R.string.never_permission_description);
        }
    }
}
