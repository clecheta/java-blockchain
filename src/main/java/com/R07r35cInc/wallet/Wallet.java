package com.R07r35cInc.wallet;

import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.util.ECDSA;
import com.R07r35cInc.util.Parse;

import java.math.BigDecimal;
import java.security.KeyPair;
import java.util.List;
import java.util.Map;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class Wallet {

    public static final BigDecimal STARTING_BALANCE = new BigDecimal(100); //TODO: read from config file?

    private final ECDSA ECDSA; //utility class to work with ECDSA algorithms
    private final KeyPair keyPair;
    public BigDecimal balance;
    public String publicKey;

    public Wallet() {
        ECDSA = new ECDSA();
        this.keyPair = ECDSA.genKeyPair();
        this.publicKey = ECDSA.getPublic();
        this.balance = STARTING_BALANCE;
    }

    /**
     * Generate Signatures:
     * verify the signature of other wallets: we don't want to accept any transactions
     * unless the actual underling wallet gave a proper signature with its private key.
     */
    public String sign(Object data) {
        String serializedData = new Parse<>().toJson(data);
        String signature = ECDSA.sign(serializedData);
        return signature;
    }

    public Transaction createTransaction(String recipient, BigDecimal amount) {
        if (amount.compareTo(this.balance) == 1) { //greater than val
            throw new RuntimeException("Amount exceeds balance");
        }

        return new Transaction(this, recipient, amount);
    }

    public Transaction createTransaction(String recipient, BigDecimal amount, List<Block> chain) {
        if (chain != null) {
            this.balance = Wallet.calculateBalance(chain, this.publicKey);
        }
        return  this.createTransaction(recipient, amount);
    }

    /** Goes to every transaction in the chain and if the address has an output
     * within the transaction output map, we add that to the outputs total */
    public static BigDecimal calculateBalance(List<Block> chain, String address) {
        BigDecimal outputsTotal = BigDecimal.ZERO;

        Block block;
        List<Transaction> transactionList;
        for(int i=0; i < chain.size(); i++) {
            block = chain.get(i);
            transactionList = (List<Transaction>) block.getData();
            if (transactionList != null) { //wallet has received a transaction
                for (Transaction transaction : transactionList) {
                    BigDecimal addressOutput = transaction.outputMap.get(address);
                    if (addressOutput != null) {
                        outputsTotal = outputsTotal.add(addressOutput);
                    }
                }
            }
        }

        return STARTING_BALANCE.add(outputsTotal);
    }

}
