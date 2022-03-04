package com.R07r35cInc.cryptocurrency;

import com.R07r35cInc.blockchain.Block;
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

    public static final BigDecimal STARTING_BALANCE = new BigDecimal(100); //TODO: read from config file?

    private final ECDSA ECDSA; //utility class to work with ECDSA algorithms
    public final KeyPair keyPair;
    public BigDecimal balance;
    public String publicKey;
    public String uid; //the uuid or the tenantId that identifies a unique user from the app

    public Wallet() {
        ECDSA = new ECDSA();
        this.keyPair = ECDSA.genKeyPair();
        this.publicKey = ECDSA.getPublic();
        this.balance = STARTING_BALANCE;
        this.uid = UUID.randomUUID().toString();
    }

    public Wallet(KeyPair keyPair, BigDecimal balance, String uid) {
        ECDSA = new ECDSA();
        ECDSA.setKeyPair(keyPair);
        this.keyPair = keyPair;
        this.publicKey = ECDSA.getPublic();
        this.balance = balance;
        this.uid = uid;
    }

    /**
     * Goes to every transaction in the chain and if the address has an output
     * within the transaction output map, we add that to the outputs total
     */
    public static BigDecimal calculateBalance(List<Block<List<Tx>>> chain, String address) {
        Boolean hasConductedTransaction = false;
        BigDecimal outputsTotal = BigDecimal.ZERO;

        Block<List<Tx>> block;
        List<Tx> transactionList;
        //iterate top-down as recent transactions are in the top of the list
        for (int i = chain.size()-1; i > 0; i--) { //do not iterate over Genesis data
            block = chain.get(i);
            transactionList = block.getData();
            if (transactionList != null) { //wallet has received a transaction
                for (Tx transaction : transactionList) {
                    if (transaction.getInput().address.equals(address)) {
                        /* Do not double count outputs. If the address already made a transaction
                           the balance should start from the output for the address*/
                        hasConductedTransaction = true;
                    }

                    BigDecimal addressOutput = transaction.getOutputMap().get(address);
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
            this.balance = Wallet.calculateBalance(chain, this.publicKey);
        }
        return this.createTransaction(cmd);
    }

}
