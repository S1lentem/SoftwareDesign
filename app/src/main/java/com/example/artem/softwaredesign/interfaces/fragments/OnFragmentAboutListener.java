package com.example.artem.softwaredesign.interfaces.fragments;

import com.example.artem.softwaredesign.data.PermissionState;

public interface OnFragmentAboutListener {
    String getVersion();
    String getImei();
    void requestPermissionForImei();
    String getDescriptionPermission();
}
