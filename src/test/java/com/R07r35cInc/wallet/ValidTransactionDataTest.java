package com.R07r35cInc.wallet;

import com.BaseTest;
import com.R07r35cInc.blockchain.NetChain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class ValidTransactionDataTest {
    NetChain<List<Transaction>> cryptoNetChain;
    Transaction transaction, rewardTransaction;
    Wallet wallet;
    List<Transaction> transactionList;

    BigDecimal tooHighAmount = new BigDecimal(999999);

    @BeforeEach
    void init() {
        cryptoNetChain = new NetChain<>();
        wallet = new Wallet();
        transaction = new Transaction(wallet, BaseTest.publicKey, new BigDecimal(65));
        rewardTransaction = Transaction.rewardTransaction(wallet);
        transactionList = new ArrayList<>();
    }

    @Test
    void dataIsValid() {
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        cryptoNetChain.addBlock(transactionList);

        Boolean isValid = cryptoNetChain.validTransactionData(cryptoNetChain.getChain());
        assertTrue(isValid, "the transaction data is valid");
    }

    @Test
    void multipleRewards() {
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        transactionList.add(rewardTransaction);
        cryptoNetChain.addBlock(transactionList);

        assertFalse(cryptoNetChain.validTransactionData(cryptoNetChain.getChain()),
                "the transaction data has multiple rewards");
    }

    //transaction data has at least one malformed outputMap cases

    @Test
    void notRewardTransaction() {
        transaction.outputMap.put(wallet.publicKey, tooHighAmount);
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        cryptoNetChain.addBlock(transactionList);

        assertFalse(cryptoNetChain.validTransactionData(cryptoNetChain.getChain()),
                "the transaction is not a reward transaction - valid data?");
    }

    @Test
    void isRewardTransaction() {
        rewardTransaction.outputMap.put(wallet.publicKey, tooHighAmount);
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        cryptoNetChain.addBlock(transactionList);

        assertFalse(cryptoNetChain.validTransactionData(cryptoNetChain.getChain()),
                "the transaction is a reward transaction - valid data?");
    }

    /*
    @Test
    void oneMalformedInput() {
        wallet.balance = new BigDecimal(9000);

        Map<String, BigDecimal> evilOutputMap = new HashMap<>();
        evilOutputMap.put(wallet.publicKey, new BigDecimal(8900));
        evilOutputMap.put("fooRecipient", new BigDecimal(100));

        Input evilInput = new Input();
        evilInput.date = new Date();
        evilInput.amount = wallet.balance;
        evilInput.address = wallet.publicKey;
        evilInput.signature = wallet.sign(evilOutputMap);

        Transaction evilTransaction = new Transaction();
        evilTransaction.input = evilInput;
        evilTransaction.outputMap = evilOutputMap;

        transactionList.add(evilTransaction);
        transactionList.add(rewardTransaction);
        cryptoNetChain.addBlock(transactionList);

        assertFalse(cryptoNetChain.validTransactionData(cryptoNetChain.getChain()),
                "the transaction data has at least one malformed input - valid data?");
    }

    @Test
    void multipleIdenticalTransactions() {
        transactionList.add(transaction);
        transactionList.add(transaction);
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        cryptoNetChain.addBlock(transactionList);

        //Enforce the blockchain to have a unique transaction set when it comes in
        assertFalse(cryptoNetChain.validTransactionData(cryptoNetChain.getChain()),
                "a block contains multiple identical transactions - valid data?");

    }*/

}