package com.example.artem.softwaredesign.interfaces.fragments;

import com.example.artem.softwaredesign.data.exceptions.about.NotAccessToImeiException;

public interface OnFragmentAboutListener {
    String getVersion();
    String getImei() throws NotAccessToImeiException;
    void requestPermissionForImei();
    String getDescriptionPermission();
}
