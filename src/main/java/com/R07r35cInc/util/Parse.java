package com.R07r35cInc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.ByteBuffer;
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
}
