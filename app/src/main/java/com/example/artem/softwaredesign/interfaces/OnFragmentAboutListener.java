package com.example.artem.softwaredesign.interfaces;

import com.example.artem.softwaredesign.data.PermissionState;

public interface OnFragmentAboutListener {
    String getVersion();
    String getImei();
    void requestPermissionForImei();
    String getDescriptionPermission();
}
