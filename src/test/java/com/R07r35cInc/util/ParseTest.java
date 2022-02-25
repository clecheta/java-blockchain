package com.R07r35cInc.util;

import com.BaseTest;
import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.blockchain.NetChain;
import com.R07r35cInc.cryptocurrency.Transaction;
import com.R07r35cInc.cryptocurrency.Wallet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class ParseTest extends BaseTest {

    NetChain<List<Transaction>> cryptoChain;
    List<Transaction> transactions;

    Transaction transaction;
    Wallet senderWallet;

    @BeforeEach
    void init() {
        cryptoChain = new NetChain<List<Transaction>>();
        transactions = new ArrayList<Transaction>();
        for (int i = 0; i < 6; i++) {
            senderWallet = new Wallet();
            transaction = new Transaction(
                    senderWallet,
                    publicKey,
                    new BigDecimal(i));
            transactions.add(transaction);
        }
        cryptoChain.addBlock(transactions);
    }

    @Test
    void cryptoChainSerDes() {
        Parse<List<Block<List<Transaction>>>> parse = new Parse();
        String originalChainString = Parse.toJson(cryptoChain.getChain());

        List<Block<List<Transaction>>> parsedChain = parse.fromJson(originalChainString,
                new TypeReference<List<Block<List<Transaction>>>>() {
                });
        String parsedChainString = Parse.toJson(parsedChain);

        assertTrue(cryptoChain.getChain().size() > 1,
                "original chain must have mined blocks for this test");
        assertEquals(originalChainString, parsedChainString, "chain string must match");
    }

    /**
     * Crucial verification for CryptoHash functions that applies to blockchain broadcast
     * to work properly
     */
    @Test
    void blockSerDes() {
        Block originalBlock = cryptoChain.getChain().get(1);
        Parse<Block<List<Transaction>>> parse = new Parse();
        String originalBlockString = Parse.toJson(originalBlock);

        Block parsedBlock = parse.fromJson(originalBlockString, new TypeReference<Block<List<Transaction>>>() {
        });
        String parsedBlockString = Parse.toJson(parsedBlock);
        assertEquals(originalBlockString, parsedBlockString, "block json string must match");

        String originalHash = CryptoHash.calculateHash(originalBlock.getDate(), originalBlock.getPreviousHash(),
                originalBlock.getData(), originalBlock.getNumber(), originalBlock.getNonce());
        String actualHash = CryptoHash.calculateHash(parsedBlock);
        assertEquals(originalHash, actualHash, "hash from original data, and parsed block must match");

        assertEquals(originalBlock.getData(), parsedBlock.getData(), "data must match");
        assertTrue(originalBlock.equals(parsedBlock), "blocks must be equals() each other");
    }

    @Test
    /** Serialization-Deserialization with java.util.map Transaction.outputMap */
    void transactionSerDes() {
        Parse<Transaction> parse = new Parse();

        Transaction originalTransaction = transactions.get(3);
        String json = Parse.toJson(originalTransaction);
        Transaction actualTransaction = parse.fromJson(json, new TypeReference<Transaction>() {
        });

        assertEquals(originalTransaction.getId(), actualTransaction.getId());
        assertEquals(originalTransaction.getInput(), actualTransaction.getInput());
        assertEquals(originalTransaction.getOutputMap(), actualTransaction.getOutputMap());
        assertEquals(originalTransaction, actualTransaction);
    }

    @Test
    /** Serialization-Deserialization with java.util.List from a JSON array string */
    void transactionListSerDes() {
        String transactionsJSON = Parse.toJson(transactions);

        List<Transaction> parsedTransactionList = new Parse<List<Transaction>>().fromJson(transactionsJSON,
                new TypeReference<List<Transaction>>() {
                });

        assertEquals(transactions.size(), parsedTransactionList.size(),
                "the size of original transaction list and parsed one must be the same");
        assertEquals(transactions, parsedTransactionList);
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            assertTrue(t.equals(parsedTransactionList.get(i)),
                    "each original transaction must have its exactly match in the parsed array");
        }
    }

    @Test
    /** Serialization-Deserialization with java.util.Map from JSON string */
    void outputMapSerDes() {
        Map<String, BigDecimal> originalMap = transactions.get(0).getOutputMap();
        //let's add more values to the original to enrich the test
        for (Transaction t : transactions) {
            Iterator<Map.Entry<String, BigDecimal>> iterator = t.getOutputMap().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BigDecimal> entry = iterator.next();
                originalMap.put(entry.getKey(), entry.getValue());
            }
        }

        String mapJson = Parse.toJson(originalMap);
        Map<String, BigDecimal> parsedMap = new Parse<Map<String, BigDecimal>>().fromJson(mapJson,
                new TypeReference<Map<String, BigDecimal>>() {
                });
        assertEquals(originalMap.size(), parsedMap.size(), "parsed map must have the same size");
        assertEquals(originalMap, parsedMap, "parsed map must be equals");
    }

    @Test
    /** Validate the parse method used in the Parse.java */
    void nativeParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Block<List<Transaction>> originalBlock = cryptoChain.getChain().get(1);
        String originalBlockString = mapper.writeValueAsString(originalBlock);

        Block parsedBlock = mapper.readValue(originalBlockString, new TypeReference<Block<List<Transaction>>>() {
        });
        assertTrue(originalBlock.equals(parsedBlock), "blocks must be equals() each other");

        String chainString = mapper.writeValueAsString(cryptoChain.getChain());
        List<Block<List<Transaction>>> parsedChain = mapper.readValue(chainString,
                new TypeReference<List<Block<List<Transaction>>>>() {
                });
        assertEquals(cryptoChain.getChain().size(), parsedChain.size());
    }

    @Test
    void wallet() {
        WalletInfo walletInfo = new WalletInfo();
        walletInfo.address = "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAE6ASsjFjpXSupzBKctGSycNB0QUYLzlFF4agqRJhtPpVUu63Amm28aQsxoG7pfSu5ibELAgqgfmsx+ZUlPj027g==";
        walletInfo.balance = new BigDecimal(81);

        String expectedJson = "{\"address\":\"MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAE6ASsjFjpXSupzBKctGSycNB0QUYLzlFF4agqRJhtPpVUu63Amm28aQsxoG7pfSu5ibELAgqgfmsx+ZUlPj027g==\",\"balance\":81}";
        String actualJson = Parse.toJson(walletInfo);
        assertEquals(expectedJson, actualJson);
    }

}

class WalletInfo {
    public String address;
    public BigDecimal balance;
}