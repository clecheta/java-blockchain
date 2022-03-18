package com.R07r35cInc.cryptocurrency;

import com.R07r35cInc.util.Calculate;
import com.R07r35cInc.util.ECDSA;
import com.R07r35cInc.util.Parse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
@JsonPropertyOrder({"id", "input", "outputMap","tx"})
public class Transaction implements Tx {
    public String tx = "transaction";

    private UUID id;
    private Input input;
    private Map<String, BigDecimal> outputMap;

    public Transaction() {
        this.id = UUID.randomUUID();
    }

    /**
     * Only wallets should be able to create transactions
     */
    public Transaction(Wallet senderWallet, String recipient, BigDecimal amount) {
        this();
        //use outputMap so that we can have more than one recipient in the transaction
        this.outputMap = createOutputMap(senderWallet, recipient, amount);
        this.input = createInput(senderWallet, this.outputMap);
    }

    /**
     * Override constructor to work with mining transactions
     */
    public Transaction(Map<String, BigDecimal> outputMap, Input input) {
        this();
        this.outputMap = outputMap;
        this.input = input;
    }

    public static Input createRewardInput(String inputAddress) {
        return new Input(new Date(),
                BigDecimal.ZERO,
                inputAddress,
                "");
    }

    /**
     * Validate if the input amount is equal to the total values contained in the outputMap
     */
    public static boolean isValidTransaction(Tx transaction) {
        String address = transaction.getInput().address;
        BigDecimal amount = transaction.getInput().amount;
        String signature = transaction.getInput().signature;
        Map<String, BigDecimal> outputMap = transaction.getOutputMap();

        BigDecimal outputTotal = Calculate.mapTotal(outputMap);
        if (amount.compareTo(outputTotal) != 0) {
            System.out.println(LocalDateTime.now() + " ERROR [Transaction] Invalid transaction from address: " + address);
            return false;
        }

        ECDSA ECDSA = new ECDSA();
        String serializedData = new Parse<Map<String, BigDecimal>>().toJson(outputMap);
        if (!ECDSA.verifySignature(address, serializedData, signature)) {
            System.out.println(LocalDateTime.now() + " ERROR [Transaction] Invalid signature from address: " + address);
            return false;
        }

        return true;
    }

    public static Tx rewardTransaction(Wallet minerWallet, String REWARD_INPUT, BigDecimal MINER_REWARD) {
        Map<String, BigDecimal> outputMap = new HashMap<>();
        outputMap.put(minerWallet.publicKey, MINER_REWARD);
        Input input = createRewardInput(REWARD_INPUT);

        return new Transaction(outputMap, input);
    }

    private Map createOutputMap(Wallet senderWallet, String recipient, BigDecimal amount) {
        Map<String, BigDecimal> outputMap = new HashMap<String, BigDecimal>();

        outputMap.put(recipient, amount);
        outputMap.put(senderWallet.publicKey, senderWallet.balance.subtract(amount));

        return outputMap;
    }

    private Input createInput(Wallet senderWallet, Map<String, BigDecimal> outputMap) {
        return new Input(new Date(),
                senderWallet.balance,
                senderWallet.publicKey,
                senderWallet.sign(outputMap));
    }

    public void update(Wallet senderWallet, String recipient, BigDecimal amount) {
        BigDecimal senderBalance = this.outputMap.get(senderWallet.publicKey);
        if (amount.compareTo(senderBalance) == 1) {
            throw new RuntimeException("Amount exceeds balance");
        }

        if (this.outputMap.get(recipient) == null) { //if the recipient does not exist in the outputMap
            this.outputMap.put(recipient, amount);
        } else { //the recipient already exists
            BigDecimal currentAmount = this.outputMap.get(recipient);
            BigDecimal newAmount = currentAmount.add(amount);
            this.outputMap.put(recipient, newAmount);
        }

        BigDecimal newAmount = this.outputMap.get(senderWallet.publicKey).subtract(amount);
        this.outputMap.put(senderWallet.publicKey, newAmount);

        this.input = this.createInput(senderWallet, this.outputMap);
    }

    //TODO: Make test for equals() and hasCode() comparison implementation
    // Block must not have identical transactions

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(outputMap, that.outputMap) &&
                Objects.equals(input, that.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, outputMap, input);
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public Input getInput() {
        return this.input;
    }

    @Override
    public void setInput(Input input) {
        this.input = input;
    }

    @Override
    public Map<String, BigDecimal> getOutputMap() {
        return this.outputMap;
    }

    @Override
    public void setOutputMap(Map<String, BigDecimal> outputMap) {
        this.outputMap = outputMap;
    }

}