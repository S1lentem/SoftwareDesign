package com.example.artem.softwaredesign.support;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class TextManager {
    private TextManager() {}

    public static boolean isEmpty(String ... strings){
        ArrayList<String> emptyStrings = new ArrayList<>();
        for (String string: strings) {
            if (string.isEmpty()){
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    public static <T> boolean isOneOf(T self, T ... others){
        for (T other: others){
            if (self.equals(other)){
                return true;
            }
        }
        return false;
    }
}
