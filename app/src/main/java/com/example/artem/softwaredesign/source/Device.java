package com.example.artem.softwaredesign.source;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.artem.softwaredesign.BuildConfig;
import com.example.artem.softwaredesign.R;

public class Device extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 1;

    private Button buttonPermissionDescription;
    private TextView imeiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        TextView versionView = findViewById(R.id.version);
        buttonPermissionDescription = findViewById(R.id.update);
        imeiView = findViewById(R.id.ImeiInfo);

        if (getResources().getBoolean(R.bool.orientation_is_portrait)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        versionView.setText(String.format(getResources().getString(R.string.version_title), BuildConfig.VERSION_NAME));


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(Manifest.permission.READ_PHONE_STATE);
        } else {
            imeiView.setText(getImei(getResources().getString(R.string.default_for_imei)));
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_PHONE_STATE: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imeiView.setText(getImei(getResources().getString(R.string.default_for_imei)));
                    disablePermissionDescription();
                }
                else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
                        enablePermissionsDescription(getResources().getString(R.string.permission_description),
                                Manifest.permission.READ_PHONE_STATE);
                    }
                    else {
                        enablePermissionsDescription(getResources().getString(R.string.never_permission_description),
                                Manifest.permission.READ_PHONE_STATE);
                    }
                }
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    private void requestPermissions(String... permissions) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {

            final Snackbar descriptionBar = Snackbar.make(findViewById(R.id.root),
                    getResources().getString(R.string.permission_description),
                    Snackbar.LENGTH_INDEFINITE);

            descriptionBar.setAction(getResources().getString(R.string.ok),
                    view -> ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION_READ_PHONE_STATE))
                    .show();

        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
        }
    }


    private String getImei(String defaultString) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            return manager.getDeviceId();
        }
        return  defaultString;
    }



    private void enablePermissionsDescription(String description, String permission) {
        final Snackbar bar = Snackbar.make(findViewById(R.id.root), description, Snackbar.LENGTH_INDEFINITE);

        buttonPermissionDescription.setText(getResources().getString(R.string.name_for_update_button));
        buttonPermissionDescription.setVisibility(View.VISIBLE);

        buttonPermissionDescription.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                imeiView.setText(getImei(getResources().getString(R.string.default_for_imei)));
                disablePermissionDescription();
            }
            else {
                bar.setAction(getResources().getString(R.string.ok),
                        v1 -> ActivityCompat.requestPermissions(this, new String[]{permission},
                                REQUEST_CODE_PERMISSION_READ_PHONE_STATE)).show();
            }
        });
    }

    private void disablePermissionDescription() {
        buttonPermissionDescription.setVisibility(View.INVISIBLE);
    }
}