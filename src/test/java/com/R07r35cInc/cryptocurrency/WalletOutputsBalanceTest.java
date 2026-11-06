package com.R07r35cInc.cryptocurrency;

import com.BaseTest;
import com.R07r35cInc.blockchain.BlockChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class WalletOutputsBalanceTest extends BaseTest {

    Wallet wallet;
    BlockChain cryptoChain;
    Tx transaction1;
    Tx transaction2;

    @BeforeEach
    void init() {
        wallet = new Wallet(STARTING_BALANCE);
        cryptoChain = new BlockChain<List<Transaction>>();

        TransactionCommand transactCmdOne = new TransactionCommand(wallet.publicKey, new BigDecimal(5));
        transaction1 = new Wallet(STARTING_BALANCE).createTransaction(transactCmdOne);
        TransactionCommand transactCmdTwo = new TransactionCommand(wallet.publicKey, new BigDecimal(10));
        transaction2 = new Wallet(STARTING_BALANCE).createTransaction(transactCmdTwo);

        List<Tx> transactionList = new ArrayList<>();
        transactionList.add(transaction1);
        transactionList.add(transaction2);
        cryptoChain.addBlock(transactionList);
    }

    @Test
    void sumAllOutputs() {
        BigDecimal actualBalance = wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        BigDecimal totalAmount = STARTING_BALANCE
                .add(transaction1.getOutputMap().get(wallet.publicKey))
                .add(transaction2.getOutputMap().get(wallet.publicKey));
        assertEquals(actualBalance, totalAmount, "adds the sum of all outputs to the wallet balance");
    }

    @Test
    void createTransactionUsesChainBalance() {
        Wallet sender = new Wallet(STARTING_BALANCE);
        TransactionCommand cmd = new TransactionCommand(wallet.publicKey, new BigDecimal(10));
        Tx tx = sender.createTransaction(cmd, cryptoChain.getChain());
        assertNotNull(tx);
        assertEquals(STARTING_BALANCE.subtract(new BigDecimal(10)), tx.getOutputMap().get(sender.publicKey));
    }

    @Test
    void chainIsPassed() {
        TransactionCommand transactCmd = new TransactionCommand(wallet.publicKey, new BigDecimal(10));
        Tx t3 = new Wallet(STARTING_BALANCE).createTransaction(transactCmd, cryptoChain.getChain());

        assertNotNull(t3);
        List<Tx> transactionList = new ArrayList<>();
        transactionList.add(t3);
        cryptoChain.addBlock(transactionList);

        BigDecimal expectedBalance = new BigDecimal(125);
        BigDecimal actualBalance = wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(expectedBalance, actualBalance);
    }

}
