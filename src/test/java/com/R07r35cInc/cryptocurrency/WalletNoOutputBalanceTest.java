package com.R07r35cInc.cryptocurrency;

import com.R07r35cInc.blockchain.BlockChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class WalletNoOutputBalanceTest {

    Wallet wallet;
    BlockChain cryptoChain;

    @BeforeEach
    void init() {
        wallet = new Wallet();
        cryptoChain = new BlockChain<List<Transaction>>();
    }

    @Test
    void noOutputsForTheWallet() {
        BigDecimal actualValue = Wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(Wallet.STARTING_BALANCE, actualValue, "must return the STARTING_BALANCE");
    }

}