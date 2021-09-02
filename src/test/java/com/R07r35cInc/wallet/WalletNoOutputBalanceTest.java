package com.R07r35cInc.wallet;

import com.R07r35cInc.blockchain.SimpleChain;
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
    SimpleChain cryptoChain;

    @BeforeEach
    void init() {
        wallet = new Wallet();
        cryptoChain = new SimpleChain<List<Transaction>>();
    }

    @Test
    void noOutputsForTheWallet() {
        BigDecimal actualValue = Wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(Wallet.STARTING_BALANCE, actualValue, "must return the STARTING_BALANCE");
    }

}