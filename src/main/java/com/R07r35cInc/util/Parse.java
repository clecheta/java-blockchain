package com.R07r35cInc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class Parse<T> {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Static method - no need to create an instance to use it
     */
    public static String toJson(Object data) {
        String dataAsString = data.toString();
        try {
            dataAsString = mapper.writeValueAsString(data);
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
}
