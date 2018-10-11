package com.example.artem.softwaredesign.source;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.artem.softwaredesign.BuildConfig;
import com.example.artem.softwaredesign.R;


public class Device extends AppCompatActivity {
    private final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 1;
    private DevicePhoneData managerDeviceData;
    private TextView imeiView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        TextView versionView = findViewById(R.id.version);
        imeiView = findViewById(R.id.ImeiInfo);
        managerDeviceData = new DevicePhoneData(this);

        versionView.setText(getResources().getString(R.string.version_title) + BuildConfig.VERSION_NAME);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (savedInstanceState != null) {
                imeiView.setText(savedInstanceState.getString("imei"));
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
            }
        } else {
            imeiView.setText(getResources().getString(R.string.imei_title) +
                    managerDeviceData.getImei(true));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_PHONE_STATE: {
                imeiView.setText(managerDeviceData.getImei(true));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("imei", imeiView.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }
}
