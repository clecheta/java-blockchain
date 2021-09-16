package com.R07r35cInc.blockchain;

import com.R07r35cInc.cryptocurrency.Transaction;
import com.R07r35cInc.cryptocurrency.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A cryptocurrency blockchain to be used with Transactions
 * in the Peer Network API.
 *
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class CryptoChain extends NetChain<List<Transaction>> {

    public List<Block<List<Transaction>>> getChain() {
        return super.getChain();
    }

    /**
     * Necessary because of the blockchain network interaction
     * not static method as it is specific to each instance of the blockchain
     **/
    public Boolean replaceChain(List<Block<List<Transaction>>> chain) {
        if (!this.isValidTransactionData(chain)) {
            throw new RuntimeException("The incoming chain has invalid data");
        }
        return super.replaceChain(chain);
    }

    //TODO: make a test to ensure this is a non static method
    //FIXME Is it really necessary to pass the chain here as argument?
    public Boolean isValidTransactionData(List<Block<List<Transaction>>> chain) {
        List<Transaction> transactions;
        try {
            for (int i = 1; i < chain.size(); i++) { //skip the Genesis block
                Block<List<Transaction>> block = chain.get(i);
                transactions = block.getData();
                Set<Transaction> transactionSet = new HashSet<>();
                int rewardTransactionCount = 0;

                if (transactions != null) { //there is transactions in the block
                    for (Transaction transaction : transactions) {
                        if (transaction.input.address.equals(Transaction.REWARD_INPUT)) {
                            rewardTransactionCount += 1;

                            if (rewardTransactionCount > 1) {
                                System.out.println(LocalDateTime.now() + " ERROR [CryptoChain] Miner reward exceed limit");
                                return false;
                            }

                            Optional<String> firstKey = transaction.outputMap.keySet().stream().findFirst();
                            BigDecimal firstValue = null;
                            if (firstKey.isPresent()) {
                                firstValue = transaction.outputMap.get(firstKey.get());
                            }
                            if (firstValue == null || firstValue.compareTo(Transaction.MINER_REWARD) != 0) {
                                System.out.println(LocalDateTime.now() + " ERROR [CryptoChain] Miner reward amount is invalid");
                                return false;
                            }
                        } else { //not a reward transaction
                            if (!Transaction.isValidTransaction(transaction)) {
                                System.out.println(LocalDateTime.now() + " ERROR [CryptoChain] Invalid transaction");
                                return false;
                            }

                            //Validate Input Balances
                            BigDecimal trueBalance = Wallet.calculateBalance(this.getChain(),
                                    transaction.input.address); //must use this. reference to the chain
                            if (transaction.input.amount.compareTo(trueBalance) != 0) {
                                //Make sure that every transaction input is based on the actual blockchain history
                                System.out.println(LocalDateTime.now() + " ERROR [CryptoChain] Invalid input amount");
                                return false;
                            }

                            //Prevent Duplicate Transactions in Block
                            if (transactionSet.contains(transaction)) {
                                System.out.println(LocalDateTime.now() +
                                        " ERROR [CryptoChain] An identical transaction appears more than once in the block");
                                return false;
                            } else {
                                transactionSet.add(transaction);
                            }
                        }
                    }
                }
            }
        } catch (ClassCastException | NullPointerException exception) {
            return false;
        }
        return true;
    }

}
