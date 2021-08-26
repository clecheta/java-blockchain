package com.R07r35cInc.util;

import com.R07r35cInc.blockchain.Block;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CryptoHash {

    public static final String stringDateFormat = "dd/MM/yyyy HH:mm:ss"; //2021-08-26T12:44:50.124+00:00
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(stringDateFormat);
    private static final ObjectMapper om = new ObjectMapper();

    //Applies Sha256 to a string and returns the result.
    private static String applySha256(String input) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our input,
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
            for (int i = 0; i < bytes.length; i++) {
                hexString.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    /**
     * To be used for unit test purpose only
     */
    public static String generateHash(String value) {
        return applySha256(value);
    }

    //IN
    public static String calculateHash(Date date, String previousHash, Object data, long index, long nonce) {
        String dataAsString = new Parse().toJson(data);
        String timestamp = dateFormat.format(date);
        String indexString = Long.toString(index);
        String nonceString = Long.toString(nonce);
        return applySha256(timestamp + previousHash + dataAsString + indexString + nonceString);
    }

    //OUT-Validation
    public static String calculateHash(Block block) {
        String dataAsString = new Parse().toJson(block.getData());
        String timestamp = dateFormat.format(block.getDate());
        String indexString = Long.toString(block.getIndex());
        String nonceString = Long.toString(block.getNonce());
        return applySha256(timestamp + block.getPreviousHash() + dataAsString + indexString + nonceString);
    }

}
