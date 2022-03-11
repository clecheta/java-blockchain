package com.R07r35cInc.cryptocurrency;

import com.BaseTest;
import com.R07r35cInc.blockchain.BlockChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class WalletNoOutputBalanceTest extends BaseTest {

    Wallet wallet;
    BlockChain cryptoChain;

    @BeforeEach
    void init() {
        wallet = new Wallet(STARTING_BALANCE);
        cryptoChain = new BlockChain<List<Transaction>>();
    }

    @Test
    void noOutputsForTheWallet() {
        BigDecimal actualValue = wallet.calculateBalance(cryptoChain.getChain(), wallet.publicKey);
        assertEquals(STARTING_BALANCE, actualValue, "must return the STARTING_BALANCE");
    }

}