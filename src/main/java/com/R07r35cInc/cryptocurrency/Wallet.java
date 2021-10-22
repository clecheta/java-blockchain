package com.R07r35cInc.cryptocurrency;

import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.util.ECDSA;
import com.R07r35cInc.util.Parse;

import java.math.BigDecimal;
import java.security.KeyPair;
import java.util.List;

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
     * Goes to every transaction in the chain and if the address has an output
     * within the transaction output map, we add that to the outputs total
     */
    public static BigDecimal calculateBalance(List<Block<List<Transaction>>> chain, String address) {
        Boolean hasConductedTransaction = false;
        BigDecimal outputsTotal = BigDecimal.ZERO;

        Block<List<Transaction>> block;
        List<Transaction> transactionList;
        //iterate top-down as recent transactions are in the top of the list
        for (int i = chain.size()-1; i > 0; i--) { //do not iterate over Genesis data
            block = chain.get(i);
            transactionList = block.getData();
            if (transactionList != null) { //wallet has received a transaction
                for (Transaction transaction : transactionList) {
                    if (transaction.input.address.equals(address)) {
                        /* Do not double count outputs. If the address already made a transaction
                           the balance should start from the output for the address*/
                        hasConductedTransaction = true;
                    }

                    BigDecimal addressOutput = transaction.outputMap.get(address);
                    if (addressOutput != null) {
                        outputsTotal = outputsTotal.add(addressOutput);
                    }
                }

                if (hasConductedTransaction) {
                    break;
                }
            }
        }

        BigDecimal result = hasConductedTransaction ? outputsTotal : STARTING_BALANCE.add(outputsTotal);
        return result;
    }

    /**
     * Generate Signatures:
     * verify the signature of other wallets: we don't want to accept any transactions
     * unless the actual underling wallet gave a proper signature with its private key.
     */
    public String sign(Object data) {
        String serializedData = Parse.toJson(data);
        String signature = ECDSA.sign(serializedData);
        return signature;
    }

    public Transaction createTransaction(String recipient, BigDecimal amount) {
        if (amount.compareTo(this.balance) == 1) { //greater than val
            throw new RuntimeException("Amount exceeds balance");
        }

        return new Transaction(this, recipient, amount);
    }

    /** This is the one that must be used in the network/cryptochain api, always passing the current version
     * of chain object as argument */
    public Transaction createTransaction(String recipient, BigDecimal amount, List<Block<List<Transaction>>> chain) {
        if (chain != null) {
            this.balance = Wallet.calculateBalance(chain, this.publicKey);
        }
        return this.createTransaction(recipient, amount);
    }

}
