package com.R07r35cInc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gson.GsonBuilder;

public class CryptoHash {

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

    public static String generateHash(String value) {
        return applySha256(value);
    }

    //TODO test coverage
    public static String calculateHash(Long timestamp, String previousHash, Object data) {
        String serializedData = new GsonBuilder().setPrettyPrinting().create().toJson(data);
        return applySha256(timestamp + previousHash + serializedData);
    }

    //TODO test coverage
    public static String calculateHash(Block block) {
        String serializedData = new GsonBuilder().setPrettyPrinting().create().toJson(block.getData());
        return applySha256(
                Long.toString(block.getTimestamp()) +
                        block.getPreviousHash() +
                        serializedData
        );
    }
}
