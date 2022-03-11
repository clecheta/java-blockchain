package com.R07r35cInc.blockchain;

import com.R07r35cInc.cryptocurrency.Transaction;
import com.R07r35cInc.cryptocurrency.Tx;
import com.R07r35cInc.cryptocurrency.Wallet;
import com.R07r35cInc.util.Calculate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A cryptocurrency blockchain to be used with Transactions
 * typed with the payment form in the Peer Network API.
 *
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class CryptoChain extends NetChain<List<Tx>> {

    public List<Block<List<Tx>>> getChain() {
        return super.getChain();
    }

    /**
     * Necessary because of the blockchain network interaction
     * not static method as it is specific to each instance of the blockchain
     **/
    public Boolean replaceChain(List<Block<List<Tx>>> chain) {
        if (!this.isValidTransactionData(chain)) {
            throw new RuntimeException("The incoming chain has invalid data");
        }
        return super.replaceChain(chain);
    }

    //TODO: make a test to ensure this is a non static method
    //FIXME Is it really necessary to pass the chain here as argument?
    public Boolean isValidTransactionData(List<Block<List<Tx>>> chain) {
        List<Tx> transactions;
        Tx lastTransaction = null;
        try {
            for (int i = 1; i < chain.size(); i++) { //skip the Genesis block
                Block<List<Tx>> block = chain.get(i);
                transactions = block.getData();
                Set<Tx> transactionSet = new HashSet<>();
                int rewardTransactionCount = 0;

                if (transactions != null) { //there is transactions in the block
                    for (Tx transaction : transactions) {
                        if (transaction.getInput().address.equals(Transaction.REWARD_INPUT)) {
                            rewardTransactionCount += 1;

                            if (rewardTransactionCount > 1) {
                                System.out.println(LocalDateTime.now() + " ERROR [CryptoChain] Miner reward exceed limit");
                                return false;
                            }

                            Optional<String> firstKey = transaction.getOutputMap().keySet().stream().findFirst();
                            BigDecimal firstValue = null;
                            if (firstKey.isPresent()) {
                                firstValue = transaction.getOutputMap().get(firstKey.get());
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

                            if (i==chain.size()-1) {
                                lastTransaction = transaction;
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

        /** Validate Input Balances to make sure that every transaction
         * input is based on the actual blockchain history
         * @link https://git.okaut.com/Jesus/JBlock/-/issues/13
         */
        if (lastTransaction == null) {
            return false;
        }
        //FIXME https://git.okaut.com/Jesus/api.blockchain/-/issues/19
        // this calc does not match when working with chain saved to the database
        //Fix the wallet value fixed in 100
        BigDecimal trueBalance = Calculate.walletBalance(this.getChain(),
                lastTransaction.getInput().address, new BigDecimal(100)); //must use this. reference to the chain
        if (lastTransaction.getInput().amount.compareTo(trueBalance) != 0) {
            System.out.println(LocalDateTime.now() + " ERROR [CryptoChain] Invalid input amount");
            return false;
        }

        return true;
    }

}
