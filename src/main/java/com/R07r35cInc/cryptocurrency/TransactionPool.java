package com.R07r35cInc.cryptocurrency;

import com.R07r35cInc.blockchain.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public class TransactionPool {

    private Map<String, Transaction> transactionMap;

    public TransactionPool() {
        this.transactionMap = new HashMap<String, Transaction>();
    }

    public void clear() {
        this.transactionMap = new HashMap<String, Transaction>();
    }

    public void setTransaction(Transaction transaction) {
        this.transactionMap.put(transaction.getId().toString(), transaction);
    }

    public void setMap(Map<String, Transaction> transactionMap) {
        this.transactionMap = transactionMap;
    }

    public Transaction existingTransaction(String inputAddress) {
        List<Transaction> transactions = transactionMap.values().stream()
                .filter(transaction -> transactionMap.values().stream()
                        .anyMatch(t -> t.getInput().address.equals(inputAddress)))
                .collect(Collectors.toList());

        if (transactions.size() > 0) {
            return transactions.get(0);
        }
        return null;
    }

    public Map<String, Transaction> getTransactionMap() {
        return transactionMap;
    }

    /**
     * Filter transactions in the poll in order to get only valid transactions
     * based on the static method Transaction.isValidTransaction()
     */
    public List<Transaction> getValidTransactions() {
        /* Before Java 8 way...
        List<Transaction> transactions = new ArrayList<Transaction>(transactionMap.values());
        List<Transaction> filteredTransaction = new ArrayList<>();
        for (Transaction t: transactions) {
            if (Transaction.isValidTransaction(t)) {
                filteredTransaction.add(t);
            }
        }*/
        return transactionMap.values().stream()
                .filter(transaction -> Transaction.isValidTransaction(transaction))
                .collect(Collectors.toList());
    }

    public void clearBlockchainTransactions(List<Block<List<Transaction>>> chainBlockList) {
        for (int i = 0; i < chainBlockList.size(); i++) {
            Block block = chainBlockList.get(i);
            try {
                if (!block.equals(Block.genesis())) {
                    List<Transaction> transactionList = (List<Transaction>) block.getData();
                    for (Transaction transaction : transactionList) {
                        this.transactionMap.remove(transaction.getId().toString());
                    }
                }
            } catch (ClassCastException | NullPointerException exceptions) {
                throw new RuntimeException("error cleaning blockchain transactions from the pool");
            }
        }
    }
}
