package com.R07r35cInc.cryptocurrency;

import com.R07r35cInc.blockchain.Block;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class TransactionPool {

    private Map<String, Tx> transactionMap;
    private final Map<String, Object> senderLocks = new ConcurrentHashMap<>();
    private final Map<String, BigDecimal> pendingOutgoing = new ConcurrentHashMap<>();

    public TransactionPool() {
        this.transactionMap = new ConcurrentHashMap<>();
    }

    public void clear() {
        this.transactionMap.clear();
        this.pendingOutgoing.clear();
    }

    public void setTransaction(Tx transaction) {
        String id = transaction.getId().toString();
        if (this.transactionMap.putIfAbsent(id, transaction) == null) {
            reserve(transaction, BigDecimal.ONE);
        }
    }

    public int size() {
        return this.transactionMap.size();
    }

    public Object senderLock(String inputAddress) {
        return senderLocks.computeIfAbsent(inputAddress, ignored -> new Object());
    }

    /**
     * Available confirmed balance after reserving all pending outgoing amounts
     * from the same sender.
     */
    public BigDecimal availableBalance(String inputAddress, BigDecimal confirmedBalance) {
        BigDecimal reserved = pendingOutgoing.getOrDefault(
                inputAddress, BigDecimal.ZERO);
        return confirmedBalance.subtract(reserved);
    }

    public void setMap(Map<String, Tx> transactionMap) {
        this.transactionMap.clear();
        this.pendingOutgoing.clear();
        if (transactionMap != null) {
            transactionMap.values().forEach(this::setTransaction);
        }
    }

    public Tx existingTransaction(String inputAddress) {
        return transactionMap.values().stream()
                .filter(transaction -> transaction.getInput() != null)
                .filter(transaction -> inputAddress.equals(transaction.getInput().address))
                .findFirst()
                .orElse(null);
    }

    public Map<String, Tx> getTransactionMap() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(transactionMap));
    }

    /**
     * Filter transactions in the poll in order to get only valid transactions
     * based on the static method Transaction.isValidTransaction()
     */
    public List<Tx> getValidTransactions() {
        /* Before Java 8 way...
        List<Transaction> transactions = new ArrayList<Transaction>(transactionMap.values());
        List<Transaction> filteredTransaction = new ArrayList<>();
        for (Transaction t: transactions) {
            if (Transaction.isValidTransaction(t)) {
                filteredTransaction.add(t);
            }
        }*/
        return new ArrayList<>(transactionMap.values()).stream()
                .filter(transaction -> Transaction.isValidTransaction(transaction))
                .collect(Collectors.toList());
    }

    public List<Tx> getValidTransactions(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        return new ArrayList<>(transactionMap.values()).stream()
                .filter(Transaction::isValidTransaction)
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Remove only transactions included in a successfully persisted block.
     * Transactions accepted after the mining snapshot must remain pending.
     */
    public void removeTransactions(List<Tx> transactions) {
        if (transactions == null) {
            return;
        }
        transactions.forEach(transaction -> {
            Tx removed = this.transactionMap.remove(
                    transaction.getId().toString());
            if (removed != null) {
                reserve(removed, BigDecimal.ONE.negate());
            }
        });
    }

    public void clearBlockchainTransactions(List<Block<List<Tx>>> chainBlockList) {
        for (int i = 0; i < chainBlockList.size(); i++) {
            Block block = chainBlockList.get(i);
            try {
                if (!block.equals(Block.genesis())) {
                    List<Tx> transactionList = (List<Tx>) block.getData();
                    removeTransactions(transactionList);
                }
            } catch (ClassCastException | NullPointerException exceptions) {
                throw new RuntimeException("error cleaning blockchain transactions from the pool");
            }
        }
    }

    private void reserve(Tx transaction, BigDecimal direction) {
        if (transaction.getInput() == null) {
            return;
        }
        String sender = transaction.getInput().address;
        BigDecimal change = transaction.getOutputMap()
                .getOrDefault(sender, BigDecimal.ZERO);
        BigDecimal amount = transaction.getInput().amount
                .subtract(change).multiply(direction);
        pendingOutgoing.compute(sender, (ignored, current) -> {
            BigDecimal updated = (current == null ? BigDecimal.ZERO : current)
                    .add(amount);
            return updated.compareTo(BigDecimal.ZERO) == 0 ? null : updated;
        });
    }
}
