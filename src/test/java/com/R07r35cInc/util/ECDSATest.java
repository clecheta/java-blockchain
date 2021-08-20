package com.R07r35cInc.util;

import com.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class ECDSATest extends BaseTest {

    static ECDSA ec;
    String data = "fuba";

    @BeforeAll
    public static void init() {
        ec = new ECDSA();
        ec.genKeyPair();
    }

    @Test
    void getPublic() {
        String publicKey = null;
        publicKey = ec.getPublic();
        assertNotNull(publicKey);
    }

    @Test
    void signAndVerify() {
        String signature = null;
        signature = ec.sign(data);
        assertNotNull(signature);

        Boolean verifiedMustBeTrue = ec.verifySignature(ec.getPublic(), data, signature);
        assertTrue(verifiedMustBeTrue);
    }

    @Test
    void signAndVerifyWithSerializedData() {
        String serializedData = new Parse<String>().toJson(data);

        String signature = null;
        signature = ec.sign(serializedData);
        assertNotNull(signature);

        Boolean verifiedMustBeTrue = ec.verifySignature(ec.getPublic(), serializedData, signature);
        assertTrue(verifiedMustBeTrue);
    }

}
