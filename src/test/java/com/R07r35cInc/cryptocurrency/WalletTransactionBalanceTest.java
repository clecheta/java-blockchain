package com.R07r35cInc.cryptocurrency;

import com.BaseTest;
import com.R07r35cInc.blockchain.BlockChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The Wallet has made a transaction
 *
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class WalletTransactionBalanceTest {

    Wallet wallet;
    BlockChain cryptoChain;
    Transaction t1, t2, t3, t4;

    @BeforeEach
    void init() {
        wallet = new Wallet();
        cryptoChain = new BlockChain<List<Transaction>>();
        t1 = new Wallet().createTransaction(wallet.publicKey, new BigDecimal(5));
        t2 = new Wallet().createTransaction(wallet.publicKey, new BigDecimal(10));

        //Add the transactions in the blockchain
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(t1);
        transactionList.add(t2);
        cryptoChain.addBlock(transactionList);
    }

    @Test
    void walletHasMadeTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        Transaction fstTransaction = wallet.createTransaction(BaseTest.publicKey, new BigDecimal(20));
        transactions.add(fstTransaction);
        cryptoChain.addBlock(transactions);

        BigDecimal expectedValue = fstTransaction.outputMap.get(wallet.publicKey);
        BigDecimal actualValue = Wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(expectedValue, actualValue, "must return the output amount of the recent transaction");

        Transaction scdTransaction = wallet.createTransaction(BaseTest.publicKey, new BigDecimal(10));
        transactions = new ArrayList<>();
        transactions.add(scdTransaction);
        cryptoChain.addBlock(transactions);

        expectedValue = scdTransaction.outputMap.get(wallet.publicKey);
        actualValue = Wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(expectedValue, actualValue, "must return the output amount of the recent transaction");

    }

    /** There are outputs next to and after the recent transaction */
    @Test
    void outputsNextAndAfterRecentTransaction() {
        List<Transaction> transactions = new ArrayList<>();

        Transaction recentTransaction = wallet.createTransaction(BaseTest.publicKey, new BigDecimal(50));
        Transaction sameBlockTransaction = Transaction.rewardTransaction(wallet);
        transactions.add(recentTransaction);
        transactions.add(sameBlockTransaction);
        cryptoChain.addBlock(transactions);

        Transaction nextBlockTransaction = new Wallet().createTransaction(wallet.publicKey, new BigDecimal(33));
        transactions = new ArrayList<>();
        transactions.add(nextBlockTransaction);
        cryptoChain.addBlock(transactions);

        BigDecimal expectedValue = recentTransaction.outputMap.get(wallet.publicKey)
                .add(sameBlockTransaction.outputMap.get(wallet.publicKey))
                .add(nextBlockTransaction.outputMap.get(wallet.publicKey));
        BigDecimal actualValue = Wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(expectedValue, actualValue, "includes the output amounts in the returned balance");
    }



}