package com.R07r35cInc.cryptocurrency;

/**
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 *
 * WALLET represent the amount transacted within the platafform
 * all the other payment types meam that the money didn`t pass thry us
 */
public enum PayTypeEnum {
    WALLET, REWARD, PIX, DEBIT, CREDIT, TRANSFER, MONEY, INTERCHANGE;

    public static boolean contains(String test) {
        for (PayTypeEnum selection : PayTypeEnum.values()) {
            if (selection.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
