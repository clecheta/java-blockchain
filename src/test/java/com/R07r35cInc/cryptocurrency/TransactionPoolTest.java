package com.R07r35cInc.cryptocurrency;

import com.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class TransactionPoolTest extends BaseTest {

    TransactionPool transactionPool;
    Transaction transaction;
    Wallet senderWallet;

    @BeforeEach
    void init() {
        transactionPool = new TransactionPool();
        senderWallet = new Wallet();
        transaction = new Transaction(
                senderWallet,
                recipient,
                new BigDecimal(15)
        );
    }

    @Test
    void addTransaction() {
        transactionPool.setTransaction(transaction);
        Tx expected = transactionPool.getTransactionMap().get(transaction.getId().toString());
        assertEquals(expected, transaction, "added a transaction");
    }

    @Test
    void existingTransaction() {
        transactionPool.setTransaction(transaction);
        Tx expected = transactionPool.existingTransaction(senderWallet.publicKey);
        assertEquals(expected, transaction, "returns an existing transaction given an input address");
    }

    @Test
    void reservesMultiplePendingTransactionsPerSender() {
        Wallet funded = new Wallet(new BigDecimal("100"));
        Transaction first = new Transaction(funded, recipient, new BigDecimal("10"));
        transactionPool.setTransaction(first);

        funded.balance = new BigDecimal("90");
        Transaction second = new Transaction(funded, publicKey, new BigDecimal("10"));
        transactionPool.setTransaction(second);

        assertEquals(2, transactionPool.size());
        assertEquals(new BigDecimal("80"),
                transactionPool.availableBalance(funded.publicKey, new BigDecimal("100")));
    }

}
