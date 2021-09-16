package com.R07r35cInc.blockchain;

import com.BaseTest;
import com.R07r35cInc.cryptocurrency.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class ReplaceChainTest extends BaseTest {

    BlockChain blockChain;
    BlockChain newChain;

    CryptoChain cryptoChain;
    CryptoChain newCrytoChain;

    //List<Block> originalChain;
    Block minedBlock; //just to satisfy method return;

    @BeforeEach
    void init() {
        blockChain = new BlockChain();
        newChain = new BlockChain();
        //originalChain = simpleChain.getChain();
        cryptoChain = new CryptoChain();
        newCrytoChain = new CryptoChain();
    }

    @Test
    void notLongerChain() {
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> blockChain.replaceChain(newChain.getChain()),
                "Expected replaceChain() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("The incoming chain must be longer"),
                "the new chain is not longer");
    }

    @Test
    void longerButInvalidChain() {
        minedBlock = newChain.addBlock("additional block");
        ((Block) newChain.getChain().get(1)).setPreviousHash("some-fake-hash");

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> blockChain.replaceChain(newChain.getChain()),
                "Expected replaceChain() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("The incoming chain must be valid"),
                "the new chain is invalid");
    }

    @Test
    void chainIsValid() {
        minedBlock = newChain.addBlock("Book of Exodus");
        assertDoesNotThrow(() -> {
            blockChain.replaceChain(newChain.getChain());
        });
    }

    @Test
    void validateTransactions() {
        Transaction transaction = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        newCrytoChain.addBlock(transactions); //need a bigger chain then original to call replaceChain()

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> cryptoChain.replaceChain(newCrytoChain.getChain()),
                "the validateTransactions flag is true"
        );
        assertTrue(thrown.getMessage().contains("The incoming chain has invalid data"),
                "the new chain has invalid data");
    }

}
