package com;

import com.R07r35cInc.util.Parse;

public class BaseTest {

    public static final String publicKey = "recipient-foo-public-key";

    //TODO read this value from a properties file
    public Boolean consoleLog = true;

    protected void logToConsole(Object object, String message) {
        if (consoleLog == true) {
            String blockchainJson = new Parse<>().toJson(object);
            if (message != null) {
                System.out.println("\n" + message);
            }
            System.out.println(blockchainJson);
        }
    }

}
