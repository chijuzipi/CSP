package org.cspapplier.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAHash {
    public SHAHash() {}

    public static String getHashCode(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(s.getBytes());
        byte byteData[] = md.digest();

        // Convert the byte to hex format method 1
        StringBuilder hexString = new StringBuilder();
        for (byte data : byteData) {
            String hex = Integer.toHexString(0xff & data);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
