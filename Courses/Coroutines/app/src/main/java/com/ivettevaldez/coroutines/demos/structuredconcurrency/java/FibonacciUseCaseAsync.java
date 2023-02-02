package com.ivettevaldez.coroutines.demos.structuredconcurrency.java;

import androidx.annotation.WorkerThread;

import java.math.BigInteger;

public class FibonacciUseCaseAsync {

    public interface Callback {
        void onFibonacciComputed(BigInteger result);
    }

    public void computeFibonacci(int index, Callback callback) {
        new Thread(() -> {
            BigInteger result = computeFibonacciBg(index);
            callback.onFibonacciComputed(result);
        }).start();
    }

    @WorkerThread
    private BigInteger computeFibonacciBg(int index) {
        if (index == 0) {
            return new BigInteger("0");
        } else if (index == 1) {
            return new BigInteger("1");
        } else {
            return computeFibonacciBg(index - 1).add(computeFibonacciBg(index - 2));
        }
    }
}
