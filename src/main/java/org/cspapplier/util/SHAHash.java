package org.cspapplier.util;

import org.apache.commons.codec.digest.DigestUtils;

public class SHAHash {
    public SHAHash() {}

    public static String getHashCode(String s) {
        return DigestUtils.sha1Hex(s);
    }
}
