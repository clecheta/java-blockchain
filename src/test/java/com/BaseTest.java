package com;

import com.R07r35cInc.util.Parse;

import java.math.BigDecimal;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class BaseTest {
    public static final String recipient = "j3Su2";
    public static final String publicKey = "Bar-Jesus-pu8L1C-K3y"; //https://en.wikipedia.org/wiki/Elymas
    public static final BigDecimal tooHighAmount = new BigDecimal(9000);
    public static final BigDecimal STARTING_BALANCE = new BigDecimal(100);

    //TODO read this value from a properties file
    public Boolean consoleLog = true;

    protected void logToConsole(Object object, String message) {
        if (consoleLog == true) {
            String json = Parse.toJson(object);
            if (message != null) {
                System.out.println("\n" + message);
            }
            System.out.println(json);
        }
    }

}
