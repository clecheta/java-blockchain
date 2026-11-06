package com.R07r35cInc.util;

import com.R07r35cInc.blockchain.Block;
import com.R07r35cInc.cryptocurrency.Tx;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class Calculate {

    public static BigDecimal mapTotal(Map<String, BigDecimal> map) {
        BigDecimal outputTotal = BigDecimal.ZERO;
        Iterator<Map.Entry<String, BigDecimal>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, BigDecimal> entry = iterator.next();
            outputTotal = outputTotal.add(entry.getValue());
        }
        return outputTotal;
    }

    /**
     * Goes to every transaction in the chain and if the address has an output
     * within the transaction output map, we add that to the outputs total
     */
    public static BigDecimal walletBalance(List<Block<List<Tx>>> chain, String address, BigDecimal STARTING_BALANCE) {
        BigDecimal outputsTotal = BigDecimal.ZERO;

        Block<List<Tx>> block;
        List<Tx> transactionList;
        //iterate top-down as recent transactions are in the top of the list
        for (int i = chain.size()-1; i > 0; i--) { //do not iterate over Genesis data
            block = chain.get(i);
            transactionList = block.getData();
            if (transactionList != null) { //wallet has received a transaction
                // Newest transaction first. Once the wallet spends, its change
                // output is the balance base; earlier history must not be added.
                for (int transactionIndex = transactionList.size() - 1;
                     transactionIndex >= 0; transactionIndex--) {
                    Tx transaction = transactionList.get(transactionIndex);
                    if (transaction.getInput().address.equals(address)) {
                        BigDecimal change = transaction.getOutputMap().get(address);
                        if (change != null) {
                            outputsTotal = outputsTotal.add(change);
                        }
                        return outputsTotal;
                    }

                    BigDecimal addressOutput = transaction.getOutputMap().get(address);
                    if (addressOutput != null) {
                        outputsTotal = outputsTotal.add(addressOutput);
                    }
                }
            }
        }

        return STARTING_BALANCE.add(outputsTotal);
    }

}
