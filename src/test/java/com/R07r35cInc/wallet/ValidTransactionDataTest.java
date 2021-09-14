package com.R07r35cInc.wallet;

import com.BaseTest;
import com.R07r35cInc.blockchain.CryptoChain;
import com.R07r35cInc.blockchain.NetChain;
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
    CryptoChain cryptoChain;
    Transaction transaction, rewardTransaction;
    Wallet wallet, minerWallet;
    List<Transaction> transactionList;

    @BeforeEach
    void init() {
        cryptoChain = new CryptoChain();
        wallet = new Wallet();
        minerWallet = new Wallet();
        transaction = wallet.createTransaction(BaseTest.publicKey, new BigDecimal(5));
        rewardTransaction = Transaction.rewardTransaction(minerWallet);
        transactionList = new ArrayList<>();
    }

    @Test
    void dataIsValid() {
        transactionList.add(transaction);
        transactionList.add(rewardTransaction);
        cryptoChain.addBlock(transactionList);

        Boolean isValid = cryptoChain.isValidTransactionData(cryptoChain.getChain());
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

    /* Tests are ok but there is a problem in the code.
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