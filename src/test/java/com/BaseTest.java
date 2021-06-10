package com;

import com.google.gson.GsonBuilder;

public class BaseTest {

    //TODO read this value from a properties file
    public Boolean consoleLog = true;

    protected void logToConsole(Object object) {
        if (consoleLog == true) {
            String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(object);
            System.out.println(blockchainJson);
        }
    }

}
