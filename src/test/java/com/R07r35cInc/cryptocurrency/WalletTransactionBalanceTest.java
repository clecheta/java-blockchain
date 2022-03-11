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
 * https://encontreiJesus.com
 */
public class WalletTransactionBalanceTest extends BaseTest {

    Wallet wallet;
    BlockChain cryptoChain;
    Tx transaction1, transaction2;

    @BeforeEach
    void init() {
        wallet = new Wallet(STARTING_BALANCE);
        cryptoChain = new BlockChain<List<Transaction>>();

        TransactionCommand transactCmdOne = new TransactionCommand(wallet.publicKey, new BigDecimal(5));
        transaction1 = new Wallet(STARTING_BALANCE).createTransaction(transactCmdOne);
        TransactionCommand transactCmdTwo = new TransactionCommand(wallet.publicKey, new BigDecimal(10));
        transaction2 = new Wallet(STARTING_BALANCE).createTransaction(transactCmdTwo);

        //Add the transactions in the blockchain
        List<Tx> transactionList = new ArrayList<>();
        transactionList.add(transaction1);
        transactionList.add(transaction2);
        cryptoChain.addBlock(transactionList);
    }

    @Test
    void walletHasMadeTransactions() {
        List<Tx> transactions = new ArrayList<>();

        TransactionCommand transactCmdOne = new TransactionCommand(BaseTest.publicKey, new BigDecimal(20));
        Tx fstTransaction = wallet.createTransaction(transactCmdOne);
        transactions.add(fstTransaction);
        cryptoChain.addBlock(transactions);

        BigDecimal expectedValue = fstTransaction.getOutputMap().get(wallet.publicKey);
        BigDecimal actualValue = wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(expectedValue, actualValue, "must return the output amount of the recent transaction");

        TransactionCommand transactCmdTwo = new TransactionCommand(BaseTest.publicKey, new BigDecimal(10));
        Tx scdTransaction = wallet.createTransaction(transactCmdTwo);
        transactions = new ArrayList<>();
        transactions.add(scdTransaction);
        cryptoChain.addBlock(transactions);

        expectedValue = scdTransaction.getOutputMap().get(wallet.publicKey);
        actualValue = wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(expectedValue, actualValue, "must return the output amount of the recent transaction");

    }

    /** There are outputs next to and after the recent transaction */
    @Test
    void outputsNextAndAfterRecentTransaction() {
        List<Tx> transactions = new ArrayList<>();

        TransactionCommand transactCmdOne = new TransactionCommand(BaseTest.publicKey, new BigDecimal(50));
        Tx recentTransaction = wallet.createTransaction(transactCmdOne);
        Tx sameBlockTransaction = Transaction.rewardTransaction(wallet);
        transactions.add(recentTransaction);
        transactions.add(sameBlockTransaction);
        cryptoChain.addBlock(transactions);

        TransactionCommand transactCmdTwo = new TransactionCommand(wallet.publicKey, new BigDecimal(33));
        Tx nextBlockTransaction = new Wallet(STARTING_BALANCE).createTransaction(transactCmdTwo);
        transactions = new ArrayList<>();
        transactions.add(nextBlockTransaction);
        cryptoChain.addBlock(transactions);

        BigDecimal expectedValue = recentTransaction.getOutputMap().get(wallet.publicKey)
                .add(sameBlockTransaction.getOutputMap().get(wallet.publicKey))
                .add(nextBlockTransaction.getOutputMap().get(wallet.publicKey));
        BigDecimal actualValue = wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(expectedValue, actualValue, "includes the output amounts in the returned balance");
    }

}