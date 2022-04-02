package com.R07r35cInc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class Parse<T> {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Static method - no need to create an instance to use it
     */
    public static String toJson(Object data) {
        return getString(data, false);
    }

    public static String toPrettyJson(Object data) {
        return getString(data, true);
    }

    private static String getString(Object data, Boolean pretty) {
        String dataAsString = data.toString();
        try {
            dataAsString = mapper.writeValueAsString(data);
            if (pretty) {
                dataAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            }
        } catch (JsonProcessingException e) {
            System.out.println(LocalDateTime.now() + " ERROR [Parse] Fail-object-to-json");
        }
        return dataAsString;
    }

    /**
     * Typed method, please instantiate to use
     */
    public T fromJson(String json, TypeReference typeReference) {
        T jsonAsObject = null;
        try {
            jsonAsObject = (T) mapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            System.out.println(LocalDateTime.now() + " ERROR [Parse] Fail-json-to-object");
        }
        return jsonAsObject;
    }

    public static byte[] longToBytes(Long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    //https://www.baeldung.com/java-byte-arrays-hex-strings

    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }

    public static byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

    private static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    private static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    /** If everything work as expected returns a md5 hash,
     * in case of error return the original string itself. */
    public static String toMD5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytesOfMessage = message.getBytes("UTF-8");
            byte[] digest = md.digest(bytesOfMessage);

            StringBuffer sb = new StringBuffer();
            for (byte b : digest)
                sb.append(String.format("%02x", b & 0xff));

            return sb.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return message;
    }

}
