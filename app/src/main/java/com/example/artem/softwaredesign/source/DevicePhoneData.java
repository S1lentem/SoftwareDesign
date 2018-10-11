package com.example.artem.softwaredesign.source;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.example.artem.softwaredesign.R;

public class DevicePhoneData {
    private Context context;

    public DevicePhoneData(Context context) {
        this.context = context;
    }

    public String getImei(boolean defaultString){
        if (ContextCompat.checkSelfPermission(
                this.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            if (defaultString){
                return this.context.getResources().getString(R.string.not_access_imei);
            }
            return null;
        }
        TelephonyManager manager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }
}
