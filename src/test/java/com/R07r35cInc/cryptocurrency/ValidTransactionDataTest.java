package com.R07r35cInc.cryptocurrency;

import com.BaseTest;
import com.R07r35cInc.blockchain.CryptoChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class ValidTransactionDataTest {
    CryptoChain cryptoChain;
    Transaction transaction, rewardTransaction;
    Wallet wallet, minerWallet;
    List<Transaction> transactionList;

    @BeforeEach
    void init() {
        cryptoChain = new CryptoChain();
        wallet = new Wallet();
        minerWallet = new Wallet();
        transaction = wallet.createTransaction(BaseTest.publicKey, new BigDecimal(5), cryptoChain.getChain());
        rewardTransaction = Transaction.rewardTransaction(minerWallet);
        transactionList = new ArrayList<>();
    }

    @Test
    void dataIsValid() { //TODO: review this method creation concept - what is trying to be achieved?
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        CryptoChain newChain = new CryptoChain();
        newChain.addBlock(transactionList);

        Boolean isValid = cryptoChain.isValidTransactionData(newChain.getChain());
        assertTrue(isValid, "the transaction data is valid");
    }

    @Test
    void multipleRewards() {
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        transactionList.add(rewardTransaction);
        cryptoChain.addBlock(transactionList);

        assertFalse(cryptoChain.isValidTransactionData(cryptoChain.getChain()),
                "the transaction data has multiple rewards");
    }

    /* TODO https://git.okaut.com/Jesus/JBlock/-/issues/13
    @Test
    void multipleTransactionBlocks() {
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        cryptoChain.addBlock(transactionList);

        Transaction scdTransaction = wallet.createTransaction(BaseTest.publicKey, new BigDecimal(3), cryptoChain.getChain());
        Transaction scdRewardTransaction = Transaction.rewardTransaction(minerWallet);
        transactionList = new ArrayList<>();
        transactionList.add(scdTransaction);
        transactionList.add(scdRewardTransaction);
        cryptoChain.addBlock(transactionList);

        System.out.println(Parse.toPrettyJson(cryptoChain));
        assertTrue(cryptoChain.isValidTransactionData(cryptoChain.getChain()));
    }*/

    @Test
    void malformedOutputMapNotRewardTransaction() {
        transaction.outputMap.put(wallet.publicKey, BaseTest.tooHighAmount);
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        cryptoChain.addBlock(transactionList);

        assertFalse(cryptoChain.isValidTransactionData(cryptoChain.getChain()),
                "the transaction is not a reward transaction - valid data?");
    }

    @Test
    void malformedOutputMapIsRewardTransaction() {
        rewardTransaction.outputMap.put(wallet.publicKey, BaseTest.tooHighAmount);
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        cryptoChain.addBlock(transactionList);

        assertFalse(cryptoChain.isValidTransactionData(cryptoChain.getChain()),
                "the transaction is a reward transaction - valid data?");
    }

    @Test
    void oneMalformedInput() {
        wallet.balance = BaseTest.tooHighAmount;

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
        CryptoChain newChain = new CryptoChain();
        newChain.addBlock(transactionList);

        assertFalse(cryptoChain.isValidTransactionData(newChain.getChain()),
                "the transaction data has at least one malformed input - valid data?");
    }

    @Test
    void multipleIdenticalTransactions() {
        transactionList.add(transaction);
        transactionList.add(transaction);
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        CryptoChain newChain = new CryptoChain();
        newChain.addBlock(transactionList);

        //Enforce the blockchain to have a unique transaction set when it comes in
        assertFalse(cryptoChain.isValidTransactionData(newChain.getChain()),
                "a block contains multiple identical transactions - valid data?");
    }

}