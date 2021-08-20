package com.R07r35cInc.wallet;

import com.R07r35cInc.util.ECDSA;
import com.R07r35cInc.util.Parse;

import java.math.BigDecimal;
import java.security.KeyPair;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class Wallet {
    private final ECDSA ECDSA; //utility class to work with ECDSA algorithms
    private final KeyPair keyPair;
    public BigDecimal balance;
    public String publicKey;

    public Wallet() {
        ECDSA = new ECDSA();
        this.keyPair = ECDSA.genKeyPair();
        this.publicKey = ECDSA.getPublic();
        this.balance = new BigDecimal(100); //TODO: read from config file
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

}
