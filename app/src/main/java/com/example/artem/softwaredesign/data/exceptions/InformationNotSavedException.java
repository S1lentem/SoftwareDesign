package com.example.artem.softwaredesign.data.exceptions;

import com.example.artem.softwaredesign.data.models.User;

public class InformationNotSavedException extends Exception{
    private User newInfo;

    public  InformationNotSavedException(User userInfoForSaved){
        newInfo = userInfoForSaved;
    }

    public User getNewInfo(){
        return this.newInfo;
    }
}
