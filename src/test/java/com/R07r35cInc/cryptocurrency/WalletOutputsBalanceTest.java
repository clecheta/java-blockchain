package com.R07r35cInc.cryptocurrency;

import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.blockchain.BlockChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
@ExtendWith(MockitoExtension.class)
//@RunWith(JUnitPlatform.class)
public class WalletOutputsBalanceTest {

    Wallet wallet;
    BlockChain cryptoChain;
    Tx transaction1;
    Tx transaction2;

    @BeforeEach
    void init() {
        wallet = new Wallet();
        cryptoChain = new BlockChain<List<Transaction>>();

        TransactionCommand transactCmdOne = new TransactionCommand(wallet.publicKey, new BigDecimal(5));
        transaction1 = new Wallet().createTransaction(transactCmdOne);
        TransactionCommand transactCmdTwo = new TransactionCommand(wallet.publicKey, new BigDecimal(10));
        transaction2 = new Wallet().createTransaction(transactCmdTwo);

        //Add the transactions in the blockchain
        List<Tx> transactionList = new ArrayList<>();
        transactionList.add(transaction1);
        transactionList.add(transaction2);
        cryptoChain.addBlock(transactionList);
    }

    @Test
    void sumAllOutputs() { //wallet received two transactions
        BigDecimal actualBalance = Wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        BigDecimal totalAmount = Wallet.STARTING_BALANCE
                .add(transaction1.getOutputMap().get(wallet.publicKey))
                .add(transaction2.getOutputMap().get(wallet.publicKey));
        assertEquals(actualBalance, totalAmount, "adds the sum of all outputs to the wallet balance");
    }

    @Test
    void ensureCalculateBalanceIsCalled() { //FIXME not really working
        Wallet mockWallet = mock(Wallet.class);
        ClasseSendoTestada classeSendoTestada = new ClasseSendoTestada(mockWallet);
        classeSendoTestada.createTransaction(wallet.publicKey, new BigDecimal(10),
                new BlockChain().getChain());
        //verify(mockWallet, times(1)).calculateBalance(cryptoChain.getChain(), mockWallet.publicKey);
    }

    @Test
    void chainIsPassed() {
        TransactionCommand transactCmd = new TransactionCommand(wallet.publicKey, new BigDecimal(10));
        Tx t3 = new Wallet().createTransaction(transactCmd, cryptoChain.getChain());

        assertNotNull(t3);
        List<Tx> transactionList = new ArrayList<>();
        transactionList.add(t3);
        cryptoChain.addBlock(transactionList);

        BigDecimal expectedBalance = new BigDecimal(125);
        BigDecimal actualBalance = Wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(expectedBalance, actualBalance);
    }

}


class ClasseSendoTestada {

    Wallet dependencia;

    public ClasseSendoTestada(Wallet dependencia){
        this.dependencia = dependencia;
    }

    void createTransaction(String recipient, BigDecimal amount, List<Block<List<Tx>>> chain){
        TransactionCommand cmd = new TransactionCommand(recipient, amount);
        dependencia.createTransaction(cmd, chain);
    }

}