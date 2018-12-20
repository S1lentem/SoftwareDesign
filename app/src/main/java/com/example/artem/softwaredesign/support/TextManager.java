package com.example.artem.softwaredesign.support;

public class TextManager {
    private TextManager() {}

    public static boolean isEmpty(String ... strings){
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
