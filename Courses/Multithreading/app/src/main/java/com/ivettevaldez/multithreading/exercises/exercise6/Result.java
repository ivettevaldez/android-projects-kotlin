package com.ivettevaldez.multithreading.exercises.exercise6;

import java.math.BigInteger;

public class Result {

    private final boolean isAborted;
    private final boolean isTimedOut;
    private final BigInteger result;

    Result(boolean isAborted, boolean isTimedOut, BigInteger result) {
        this.isAborted = isAborted;
        this.isTimedOut = isTimedOut;
        this.result = result;
    }

    boolean isAborted() {
        return isAborted;
    }

    boolean isTimedOut() {
        return isTimedOut;
    }

    public BigInteger getResult() {
        return result;
    }
}
