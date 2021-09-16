package com.R07r35cInc.cryptocurrency;

import com.BaseTest;
import com.R07r35cInc.util.ECDSA;
import com.R07r35cInc.util.Parse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class TransactionInputTest extends BaseTest {

    Transaction transaction;
    Wallet senderWallet;
    String recipient;
    BigDecimal amount;

    @BeforeEach
    void init() {
        senderWallet = new Wallet();
        recipient = publicKey;
        amount = BigDecimal.TEN;
        transaction = new Transaction(senderWallet, recipient, amount);
    }

    @Test
    void hasInput() {
        Field[] fields = Transaction.class.getDeclaredFields();
        assertEquals(true, Arrays.stream(fields).anyMatch(f -> f.getName().equals("input")),
                "has an input");
    }

    @Test
    void hasDate() {
        Field[] fields = Input.class.getDeclaredFields();
        assertEquals(true, Arrays.stream(fields).anyMatch(f -> f.getName().equals("date")),
                "has a date in the input");
    }

    @Test
    void setsAmount() {
        BigDecimal expectAmount = transaction.input.amount;
        BigDecimal actualAmount = senderWallet.balance;
        //ensure that the amount matches the balance that is contained in the sender wallet
        assertEquals(expectAmount, actualAmount, "sets the amount to the senderWallet balance");
    }

    @Test
    void setsAddress() {
        String expectedAddress = transaction.input.address;
        String actualAddress = senderWallet.publicKey;
        assertEquals(expectedAddress, actualAddress, "sets the address to the senderWallet publicKey");
    }

    @Test
    void signInput() {
        ECDSA ec = new ECDSA();
        String serializedData = new Parse().toJson(transaction.outputMap);
        assertTrue(ec.verifySignature(senderWallet.publicKey, serializedData, transaction.input.signature),
                "signs the input");
    }

}