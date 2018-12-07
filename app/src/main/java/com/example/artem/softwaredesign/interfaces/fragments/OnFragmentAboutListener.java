package com.example.artem.softwaredesign.interfaces.fragments;

public interface OnFragmentAboutListener {
    String getVersion();
    String getImei();
    void requestPermissionForImei();
    String getDescriptionPermission();
}
