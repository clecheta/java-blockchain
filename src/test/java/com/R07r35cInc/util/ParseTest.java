package com.R07r35cInc.util;

import com.BaseTest;
import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.blockchain.NetChain;
import com.R07r35cInc.wallet.Transaction;
import com.R07r35cInc.wallet.Wallet;
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
public class ParseTest extends BaseTest {

    private static final String publicKey = "recipient-public-key";

    NetChain<List<Transaction>> cryptoChain;
    List<Transaction> transactions;

    Transaction transaction;
    Wallet senderWallet;

    @BeforeEach
    void init() {
        cryptoChain = new NetChain<List<Transaction>>();
        transactions = new ArrayList<Transaction>();
        for (int i=0; i<6; i++) {
            senderWallet = new Wallet();
            transaction = new Transaction(
                    senderWallet,
                    publicKey,
                    new BigDecimal(i));
            transactions.add(transaction);
        }
        cryptoChain.addBlock(transactions);
    }

    /* This verification is crucial for CryptoHash functions to work properly */
    @Test
    void serDes() { //Serialization-Deserialization
        Parse<List<Block<List<Transaction>>>> parse = new Parse(cryptoChain.getChain().getClass());
        String originalChainString = parse.toJson(cryptoChain.getChain());

        List<Block<List<Transaction>>> parsedChain = parse.fromJson(originalChainString);
        String parsedChainString = parse.toJson(parsedChain);

        assertEquals(originalChainString, parsedChainString, "chain string must match");
    }

}
