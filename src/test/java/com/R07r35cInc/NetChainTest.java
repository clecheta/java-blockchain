package com.R07r35cInc;


import com.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class NetChainTest extends BaseTest {

    NetChain netChain;
    Block minedBlock; //just to satisfy method return;

    @BeforeEach
    void init() {
        netChain = new NetChain();
    }


}
