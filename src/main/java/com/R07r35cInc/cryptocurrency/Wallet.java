package com.R07r35cInc.cryptocurrency;

import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.util.Calculate;
import com.R07r35cInc.util.ECDSA;
import com.R07r35cInc.util.Parse;

import java.math.BigDecimal;
import java.security.KeyPair;
import java.util.List;
import java.util.UUID;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class Wallet {
    private final ECDSA ECDSA; //utility class to work with ECDSA algorithms

    public final KeyPair keyPair; //an account is the keypair for a user-owned
    public BigDecimal balance;
    public String publicKey;
    public String uid; //identifies a unique user from the app

    /** a counter that indicates the number of transactions sent from the account */
    public Long nonce = 0L;
    private BigDecimal STARTING_BALANCE = BigDecimal.ZERO;

    public Wallet() {
        ECDSA = new ECDSA();
        this.keyPair = ECDSA.genKeyPair();
        this.publicKey = ECDSA.getPublic();
        this.balance = STARTING_BALANCE;
        this.uid = UUID.randomUUID().toString();
    }

    public Wallet(BigDecimal startingBalance) {
        ECDSA = new ECDSA();
        this.keyPair = ECDSA.genKeyPair();
        this.publicKey = ECDSA.getPublic();
        this.balance = startingBalance;
        this.uid = UUID.randomUUID().toString();
        this.STARTING_BALANCE = startingBalance;
    }

    public Wallet(KeyPair keyPair, BigDecimal balance, String uid, BigDecimal startingBalance, Long nonce) {
        ECDSA = new ECDSA();
        ECDSA.setKeyPair(keyPair);
        this.keyPair = keyPair;
        this.publicKey = ECDSA.getPublic();
        this.balance = balance;
        this.uid = uid;
        this.nonce = nonce;
        this.STARTING_BALANCE = startingBalance;
    }

    /**
     * Goes to every transaction in the chain and if the address has an output
     * within the transaction output map, we add that to the outputs total
     */
    public BigDecimal calculateBalance(List<Block<List<Tx>>> chain, String address) {
        return Calculate.walletBalance(chain, address, STARTING_BALANCE);
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

    public Tx createTransaction(TransactionCommand cmd) {
        if (cmd.amount.compareTo(this.balance) == 1) { //greater than val
            throw new RuntimeException("Amount exceeds balance");
        }

        Tx transaction = new Transaction(this, cmd.recipient, cmd.amount);
        if (cmd.type != null && PayTypeEnum.contains(cmd.type.name())) { //there is an associated pay type
            PayTypeTransaction typedTransaction = new PayTypeTransaction(transaction, cmd.type);
            return typedTransaction;
        } else {
            return transaction;
        }
    }

    /** This is the one that must be used in the network/cryptochain api, always passing the current version
     * of chain object as argument */
    public Tx createTransaction(TransactionCommand cmd, List<Block<List<Tx>>> chain) {
        if (chain != null) {
            this.balance = this.calculateBalance(chain, this.publicKey);
        }
        return this.createTransaction(cmd);
    }

    public BigDecimal getSTARTING_BALANCE() {
        return STARTING_BALANCE;
    }

}
