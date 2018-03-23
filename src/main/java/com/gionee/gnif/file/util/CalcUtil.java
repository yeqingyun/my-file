package com.gionee.gnif.file.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yeqy on 2017/5/27.
 */
public class CalcUtil {


    public static String getFromBase64(String s) throws UnsupportedEncodingException {
        return new String(Base64.decodeBase64(s.getBytes("UTF-8")), "UTF-8");
    }

    public static String getBase64(String str) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(str.getBytes("UTF-8")), "UTF-8");
    }

    public static String hamcsha1(String data, String key) {
        try {
            byte[] ds = data.getBytes();
            byte[] ks = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(ks, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(ds));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString();
    }


}
