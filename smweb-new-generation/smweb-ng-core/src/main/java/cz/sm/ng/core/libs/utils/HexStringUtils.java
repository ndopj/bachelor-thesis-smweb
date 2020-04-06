package cz.sm.ng.core.libs.utils;

import cz.sm.ng.core.identity.models.Identity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Staticka trieda, ktora dokaze prevadzat medzi polom bytov a hexadecimalnym stringom.
 * @author Roman Stoklasa
 */
public class HexStringUtils {

    /**
     * Prevedie pole bytov na stringovu reprezentaciu, pouzitim hexadecimalnych znakov.
     *
     * @param b
     * @return
     * @throws Exception
     */
    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result +=
                    Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

// ======================================================================================
    /**
     * Prevedie stringovu hexa-reprezentaciu na pole bytov.
     *
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

// ======================================================================================
    /**
     * Vypocita SHA1 hash zadaneho retazca text a vrati vysledok vo forme retazca (hex-string)
     *
     * @param text
     * @return
     */
    public static String getSha1String(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text can not be null");
        }

        return byteArrayToHexString(getSha1Bytes(text));
    }

// ======================================================================================
    /**
     * Vypocita SHA1 hash zadaneho retazca text a vrati vysledok vo forme pola bytov.
     *
     * @param text
     * @return
     */
    public static byte[] getSha1Bytes(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text can not be null");
        }

        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance("SHA1");
            byte[] hashBytes = sha1.digest(text.getBytes());

            return hashBytes;

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Identity.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnsupportedOperationException("Unable to perform SHA1 hash. NoSuchAlgorithmException ocuured.", ex);
        }

    }
// ======================================================================================
}

