package com.example.ethon.car_service_station.domain;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by Ethon on 2016/10/31.
 */

public class PasswordEncryptor {

    public static String convertPasswordToMD5(String pass) throws NoSuchAlgorithmException {
        MessageDigest md=MessageDigest.getInstance("MD5");
        md.update(pass.getBytes());

        byte byteData[]=md.digest();

        StringBuffer sb=new StringBuffer();
        for(int x=0;x<byteData.length;x++)
        {
            sb.append(Integer.toString((byteData[x]&0xff)+0x100,16).substring(1));
        }
        return sb.toString();
    }
}
