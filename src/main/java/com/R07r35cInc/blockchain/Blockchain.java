package com.R07r35cInc.blockchain;

import java.util.List;

/**
 * Any blockchain must return a list of Block
 *
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public interface Blockchain<T> {
    List<Block<T>> getChain();
}
