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
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Test
    void cryptoChainSerDes() {
        Parse<List<Block<List<Transaction>>>> parse = new Parse(cryptoChain.getChain().getClass());
        String originalChainString = parse.toJson(cryptoChain.getChain());

        List<Block<List<Transaction>>> parsedChain = parse.fromJson(originalChainString);
        String parsedChainString = parse.toJson(parsedChain);

        assertTrue(cryptoChain.getChain().size()>1,
                "original chain must have mined blocks for this test");
        assertEquals(originalChainString, parsedChainString, "chain string must match");
    }

    /** Crucial verification for CryptoHash functions that applies to blockchain broadcast
     *  to work properly */
    @Test
    void blockSerDes() {
        Parse<Block> parse = new Parse(Block.class);
        Block originalBlock = cryptoChain.getChain().get(1);
        String originalBlockString = parse.toJson(originalBlock);

        Block parsedBlock = parse.fromJson(originalBlockString);
        String parsedBlockString = parse.toJson(parsedBlock);

        assertEquals(originalBlockString, parsedBlockString, "block json string must match");
        assertEquals(originalBlock.getData(), parsedBlock.getData(), "data must match");
        assertEquals(originalBlock, parsedBlock, "blocks must be equals()");

        String originalHash = CryptoHash.calculateHash(originalBlock.getDate(), originalBlock.getPreviousHash(),
                originalBlock.getData(), originalBlock.getIndex(), originalBlock.getNonce());
        String actualHash = CryptoHash.calculateHash(parsedBlock);
        assertEquals(originalHash, actualHash);
    }

    @Test
    /** Serialization-Deserialization with java.util.map Transaction.outputMap */
    void transactionSerDes() {
        Parse<Transaction> parse = new Parse(Transaction.class);

        Transaction originalTransaction = transactions.get(3);
        String json = parse.toJson(originalTransaction);
        Transaction actualTransaction = parse.fromJson(json);

        assertEquals(originalTransaction.id, actualTransaction.id);
        assertEquals(originalTransaction.input, actualTransaction.input);
        assertEquals(originalTransaction.outputMap, actualTransaction.outputMap);
        assertEquals(originalTransaction, actualTransaction);
    }

    /* TODO create test - Creating a Java List From a JSON Array String
We can parse a JSON in the form of an array into a Java object list using a TypeReference:

String jsonCarArray =
  "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
List<Car> listCar = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
     */

    /* TODO create test - Creating Java Map From JSON String
    String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
Map<String, Object> map
  = objectMapper.readValue(json, new TypeReference<Map<String,Object>>(){});
     */

}
