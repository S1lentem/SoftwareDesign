package com.example.artem.softwaredesign.source;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.example.artem.softwaredesign.BuildConfig;
import com.example.artem.softwaredesign.R;


public class Device extends AppCompatActivity {

    private interface PermissionRequestable{
        void invoke();
    }

    private final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 1;
    private TextView imeiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        TextView versionView = findViewById(R.id.version);
        imeiView = findViewById(R.id.ImeiInfo);


        versionView.setText(String.format(getResources().getString(R.string.version_title), BuildConfig.VERSION_NAME));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(Manifest.permission.READ_PHONE_STATE);
        } else {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiView.setText(String.format(getResources().getString(R.string.imei_title), manager.getDeviceId()));
        }
    }

    private void requestPermissions(String ... permissions) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_PHONE_STATE: {
                showImei(getResources().getString(R.string.not_access_imei));
            }
        }
    }


    private void showImei(String defaultString) {
        String imei;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            imei = defaultString;
        } else {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = manager.getDeviceId();
        }
        imeiView.setText(String.format(getResources().getString(R.string.imei_title), imei));
    }
}
