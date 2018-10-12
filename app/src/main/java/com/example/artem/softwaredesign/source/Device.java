package com.example.artem.softwaredesign.source;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        versionView.setText(String.format(getResources().getString(R.string.version_title), BuildConfig.VERSION_NAME));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            final Snackbar descriptionSnackbar = Snackbar.make(findViewById(R.id.root),
                    getResources().getString(R.string.permission_description),
                    Snackbar.LENGTH_INDEFINITE);

            descriptionSnackbar.setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    descriptionSnackbar.dismiss();
                }
            });
            descriptionSnackbar.show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE_PERMISSION_READ_PHONE_STATE);
        } else {
            imeiView.setText(String.format(getResources().getString(R.string.imei_title), managerDeviceData.getImei(true)));
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
