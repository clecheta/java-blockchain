package com.R07r35cInc.cryptocurrency;

import com.R07r35cInc.util.ECDSA;
import com.R07r35cInc.util.Parse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class WalletTest {

    private static final int BASE64_LENGHT = 124;

    Wallet wallet;
    String data = "Jiu-jitsu";

    @BeforeEach
    void init() {
        wallet = new Wallet();
    }

    @Test
    void hasBalance() {
        assertTrue(wallet.balance != null);
        assertTrue(wallet.balance instanceof BigDecimal);
        assertTrue(wallet.balance.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void balanceHasRightValue() {
        BigDecimal startBalance = Wallet.STARTING_BALANCE;
        assertTrue(startBalance.compareTo(new BigDecimal(100)) == 0,
                "wallet balance must have the initial configured starting balance value");
        assertEquals(startBalance, wallet.balance);
    }

    @Test
    void hasPublicKey() {
        assertTrue(wallet.publicKey != null);
        assertTrue(wallet.publicKey instanceof String);
        System.out.println(wallet.publicKey.length());
        assertTrue(wallet.publicKey.length() == BASE64_LENGHT);
    }

    @Test
    void verifySignature() {
        String signature = wallet.sign(data); //serialize data
        ECDSA ec = new ECDSA();
        /* we must serialize data before call ec.verifySignature as sign() method on Wallet does serialize the data
         * before call the ec.sign() ang generate signature */
        String serializedData = new Parse<>().toJson(data);
        assertTrue(ec.verifySignature(wallet.publicKey, serializedData, signature));
    }

    @Test
    void doesNotVerifyInvalidSignature() {
        ECDSA ec = new ECDSA();
        String serializedData = new Parse<>().toJson(data);
        assertFalse(ec.verifySignature(wallet.publicKey, serializedData, new Wallet().sign(serializedData)));
    }

}