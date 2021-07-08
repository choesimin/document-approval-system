package com.simin.approval.model.common;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordSecureManager {
    public String getSecureData(String password) {
        StringBuffer stringBuffer = new StringBuffer();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256") ;
            byte[] data = password.getBytes("UTF-8");
            byte[] hash = digest.digest(data);

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);

                if(hex.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(hex);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}