package com;

import com.R07r35cInc.util.Parse;

import java.math.BigDecimal;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class BaseTest {

    public static final String publicKey = "Bar-Jesus-pu8L1C-K3y"; //https://en.wikipedia.org/wiki/Elymas
    public static final BigDecimal tooHighAmount = new BigDecimal(9000);

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
