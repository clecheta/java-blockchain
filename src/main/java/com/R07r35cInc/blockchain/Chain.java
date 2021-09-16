package com.R07r35cInc.blockchain;

import java.util.List;

/**
 * A chain must return a list of Block
 *
 * @author Cleber Lecheta
 * https://encontreijesus.com
 */
public interface Chain<T> {
    List<Block<T>> getChain();
}
