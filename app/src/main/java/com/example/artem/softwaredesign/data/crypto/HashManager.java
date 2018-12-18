package com.example.artem.softwaredesign.data.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashManager {
    private String algorithm;


    public HashManager(EncryptionAlgorithm algorithm){
        this.algorithm = algorithm.name().replace('_', '-');
    }

    public String getHash(final String text){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b: messageDigest.digest()){
            String item = String.format("%02X", b);
            sb.append(item);
        }
        return sb.toString();
    }
}
