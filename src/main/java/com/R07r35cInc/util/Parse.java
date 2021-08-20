package com.R07r35cInc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class Parse<T> {

    private ObjectMapper om;
    private Class<T> type;

    /**
     * If you only need to parse objects toJson string
     * you can use the default constructor
     */
    public Parse() {
        this.om = new ObjectMapper();
    }

    /**
     * Use this constructor whenever you need to parse Json data
     * to native Object
     * @param type
     */
    public Parse(Class<T> type) {
        this();
        this.type = type;
    }

    public String toJson(T data) {
        String dataAsString = data.toString();
        try {
            dataAsString = om.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            System.out.println(LocalDateTime.now() + " ERROR [Parse] Fail-object-to-json");
        }
        return dataAsString;
    }

    public T fromJson(String json) {
        T jsonAsObject = null;
        try {
            jsonAsObject = om.readValue(json, type);
        } catch (JsonProcessingException e) {
            System.out.println(LocalDateTime.now() + " ERROR [Parse] Fail-json-to-object");
        }
        return jsonAsObject;
    }
}
