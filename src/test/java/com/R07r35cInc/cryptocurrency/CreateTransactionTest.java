package com.R07r35cInc.cryptocurrency;

import com.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class CreateTransactionTest extends BaseTest {

    Wallet wallet;
    Transaction transaction;
    BigDecimal amount;

    @BeforeEach
    void init() {
        wallet = new Wallet();
        amount = BigDecimal.TEN;
        TransactionCommand transactCmd = new TransactionCommand(recipient, amount);
        transaction = wallet.createTransaction(transactCmd);
    }

    @Test
    void exceedsBalance() {
        TransactionCommand transactCmd = new TransactionCommand(recipient, tooHighAmount);
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> wallet.createTransaction(transactCmd),
                "the amount exceeds the balance"
        );
        assertTrue(thrown.getMessage().contains("Amount exceeds balance"),
                "throws an error");
    }

    // Below tests for when the amount is valid

    @Test
    void createTransaction() {
        assertTrue(transaction instanceof Transaction, "creates an instance of Transaction class");
    }

    @Test
    void matchesTransactionInput() {
        String expectedValue = wallet.publicKey;
        String actualValue = transaction.getInput().address;
        assertEquals(expectedValue, actualValue, "matches the transaction input with the wallet");
    }

    @Test
    void outputAmount() {
        BigDecimal actualAmount = transaction.getOutputMap().get(recipient);
        assertEquals(amount, actualAmount, "outputs the amount the recipient");

    }

}
