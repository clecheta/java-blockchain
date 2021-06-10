package com;

import com.google.gson.GsonBuilder;

public class BaseTest {

    //TODO read this value from a properties file
    public Boolean consoleLog = true;

    protected void logToConsole(Object object, String message) {
        if (consoleLog == true) {
            String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(object);
            if (message != null) {
                System.out.println("\n" + message);
            }
            System.out.println(blockchainJson);
        }
    }

}
