package org.cspapplier.util;

import org.apache.commons.codec.digest.DigestUtils;

public class SHAHash {
    public SHAHash() {}

    public static String getHashCode(String s) {
//        MessageDigest md = MessageDigest.getInstance("SHA1");
//        md.update(s.getBytes());
//        byte byteData[] = md.digest();
//
//        // Convert the byte to hex format method 1
//        StringBuilder hexString = new StringBuilder();
//        for (byte data : byteData) {
//            String hex = Integer.toHexString(0xff & data);
//            if (hex.length() == 1) {
//                hexString.append('0');
//            }
//            hexString.append(hex);
//        }
//
//        return hexString.toString();
        return DigestUtils.sha1Hex(s);
    }
}
