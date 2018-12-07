package com.example.artem.softwaredesign.interfaces;

public interface OnFragmentAboutListener {
    String getVersion();
    String getImei();
    void requestPermissionForImei();
    String getDescriptionPermission();
}
