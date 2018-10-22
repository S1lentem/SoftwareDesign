package com.example.artem.softwaredesign.source;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.artem.softwaredesign.BuildConfig;
import com.example.artem.softwaredesign.R;

public class Device extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSION_READ_PHONE_STATE = 1;
    private final int buttonDescriptionId = 2;
    private final String keyForSaveImei = "imei";

    private LinearLayout llMain;
    private TextView imeiView;
    private String currentImei = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        TextView versionView = findViewById(R.id.version);
        imeiView = findViewById(R.id.ImeiInfo);
        llMain = findViewById(R.id.main);

        if (getResources().getBoolean(R.bool.orientation_is_portrait)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        versionView.setText(String.format(getResources().getString(R.string.version_title), BuildConfig.VERSION_NAME));


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (savedInstanceState != null){
                currentImei = savedInstanceState.getString(keyForSaveImei);
                imeiView.setText(currentImei);
            }
            else {
                requestPermissions(Manifest.permission.READ_PHONE_STATE);
            }
        } else {
            imeiView.setText(getImei(getResources().getString(R.string.default_for_imei)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_READ_PHONE_STATE: {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                    imeiView.setText(getImei(getResources().getString(R.string.default_for_imei)));
                    removeButtonById(buttonDescriptionId);
                }
                else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
                        createButtonForDescriptionPermissions(getResources().getString(R.string.name_for_update_button),
                                getResources().getString(R.string.permission_description),
                                buttonDescriptionId, Manifest.permission.READ_PHONE_STATE);
                    }
                    else {
                        createButtonForDescriptionPermissions(getResources().getString(R.string.name_for_update_button),
                                getResources().getString(R.string.never_permission_description),
                                buttonDescriptionId, Manifest.permission.READ_PHONE_STATE);
                    }
                }
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle  savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (currentImei != null) {
            savedInstanceState.putString(keyForSaveImei, currentImei);
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
            currentImei = manager.getDeviceId();
            return currentImei;

        }
        return  defaultString;
    }

    private void createButtonForDescriptionPermissions(String text, String description, int id, String... permissions) {
        final Snackbar bar = Snackbar.make(findViewById(R.id.root), description, Snackbar.LENGTH_INDEFINITE);
        Button btn = new Button(this);
        Button temp = findViewById(buttonDescriptionId);

        if (temp != null){
            btn = temp;
        }

        btn.setId(id);
        btn.setText(text);
        btn.setOnClickListener(v -> bar.setAction(getResources().getString(R.string.ok),
                v1 -> ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION_READ_PHONE_STATE)).show());

        if (temp == null) {
            llMain.addView(btn);
        }
    }

    private void removeButtonById(int id) {
        Button btn = findViewById(id);
        if (btn != null) {
            btn.setVisibility(View.GONE);
        }
    }
}