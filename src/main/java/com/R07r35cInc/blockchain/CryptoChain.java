package com.R07r35cInc.blockchain;

import com.R07r35cInc.cryptocurrency.RewardTransaction;
import com.R07r35cInc.cryptocurrency.Transaction;
import com.R07r35cInc.cryptocurrency.Tx;
import com.R07r35cInc.cryptocurrency.Wallet;
import com.R07r35cInc.util.Calculate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Boolean replaceChain(List<Block<List<Tx>>> chain, BigDecimal startingBalance) {
        if (!this.isValidTransactionData(chain, startingBalance)) {
            throw new RuntimeException("The incoming chain has invalid data");
        }
        return super.replaceChain(chain);
    }

    /**
     * Boot/recovery hydrate: trust persisted ledger bytes without replaying
     * {@link #isValidTransactionData} across the entire history.
     */
    public void hydrateFromLedger(List<Block<List<Tx>>> chain) {
        super.hydrateFromLedger(chain);
    }

    /**
     * Append a mined crypto block after structural validation, optionally
     * checking transaction data for that block in the extended chain context.
     */
    public Boolean appendValidatedBlock(Block<List<Tx>> block, BigDecimal startingBalance) {
        if (block == null) {
            throw new RuntimeException("The incoming block must not be null");
        }
        List<Block<List<Tx>>> candidate = new ArrayList<>(this.getChain());
        candidate.add(block);
        if (startingBalance != null && !this.isValidTransactionData(candidate, startingBalance)) {
            throw new RuntimeException("The incoming chain has invalid data");
        }
        return super.appendValidatedBlock(block);
    }

    @Override
    public Boolean appendValidatedBlock(Block<List<Tx>> block) {
        return appendValidatedBlock(block, null);
    }

    //TODO: make a test to ensure this is a non static method
    //FIXME Is it really necessary to pass the chain here as argument?
    public Boolean isValidTransactionData(List<Block<List<Tx>>> chain, BigDecimal startingBalance) {
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
                        if (transaction instanceof RewardTransaction) {
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

                            /*
                            if (i==chain.size()-1) {
                                lastTransaction = transaction; //TODO: maybe this should be last address transaction to be matched against true balance
                            }*/
                            BigDecimal trueBalance = Calculate.walletBalance(chain, transaction.getInput().address, startingBalance);
                            if (transaction.getInput().amount.compareTo(trueBalance) != 0) {
                                System.out.println(LocalDateTime.now() + " ERROR [CryptoChain] Invalid input amount");
                                //return false;
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
        } catch (ClassCastException exception) {
            System.out.println(LocalDateTime.now() + " ERROR [CryptoChain] ClassCastException validating txs: "
                    + exception.getMessage());
            return false;
        } catch (NullPointerException exception) {
            System.out.println(LocalDateTime.now() + " ERROR [CryptoChain] NullPointerException validating txs: "
                    + exception.getMessage());
            return false;
        }

        /** Validate Input Balances to make sure that every transaction
         * input is based on the actual blockchain history
         * @link https://git.okaut.com/Jesus/JBlock/-/issues/13

        if (lastTransaction == null) {
            return false;
        }
        //FIXME https://git.okaut.com/Jesus/api.blockchain/-/issues/19
        // this calc does not match when working with chain saved to the database
        BigDecimal trueBalance = Calculate.walletBalance(chain, lastTransaction.getInput().address, startingBalance);
        if (lastTransaction.getInput().amount.compareTo(trueBalance) != 0) {
            System.out.println(LocalDateTime.now() + " ERROR [CryptoChain] Invalid input amount");
            return false;
        }*/

        return true;
    }

}
