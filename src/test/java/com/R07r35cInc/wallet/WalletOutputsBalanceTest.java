package com.R07r35cInc.wallet;

import com.R07r35cInc.blockchain.SimpleChain;
import com.R07r35cInc.util.ECDSA;
import com.R07r35cInc.util.Parse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class WalletOutputsBalanceTest {

    Wallet wallet;
    SimpleChain cryptoChain;
    Transaction t1;
    Transaction t2;

    @BeforeEach
    void init() {
        wallet = new Wallet();
        cryptoChain = new SimpleChain<List<Transaction>>();
        t1 = new Wallet().createTransaction(wallet.publicKey, new BigDecimal(5));
        t2 = new Wallet().createTransaction(wallet.publicKey, new BigDecimal(10));

        //Add the transactions in the blockchain
        List<Transaction> transactionList = new ArrayList<Transaction>();
        transactionList.add(t1);
        transactionList.add(t2);
        cryptoChain.addBlock(transactionList);
    }

    @Test
    void sumAllOutputs() { //wallet received two transactions
        BigDecimal actualBalance = Wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        BigDecimal totalAmount = Wallet.STARTING_BALANCE
                .add(t1.outputMap.get(wallet.publicKey))
                .add(t2.outputMap.get(wallet.publicKey));
        assertEquals(actualBalance, totalAmount, "adds the sum of all outputs to the wallet balance");
    }

}