package com.R07r35cInc.blockchain;

import java.util.List;

public interface Blockchain<T> {

    List<Block<T>> getChain();

}
