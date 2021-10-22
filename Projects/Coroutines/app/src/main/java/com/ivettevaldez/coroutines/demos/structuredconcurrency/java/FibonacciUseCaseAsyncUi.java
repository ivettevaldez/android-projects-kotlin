package com.ivettevaldez.coroutines.demos.structuredconcurrency.java;

import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.math.BigInteger;

public class FibonacciUseCaseAsyncUi {

    private final BackgroundThreadPoster backgroundThreadPoster;
    private final UiThreadPoster uiThreadPoster;

    public FibonacciUseCaseAsyncUi(BackgroundThreadPoster backgroundThreadPoster,
                                   UiThreadPoster uiThreadPoster) {
        this.backgroundThreadPoster = backgroundThreadPoster;
        this.uiThreadPoster = uiThreadPoster;
    }

    public void computeFibonacci(int index, Callback callback) {
        backgroundThreadPoster.post(() -> {
            BigInteger result = computeFibonacciBg(index);
            notifyResult(result, callback);
        });
    }

    private BigInteger computeFibonacciBg(int index) {
        if (index == 0) {
            return new BigInteger("0");
        } else if (index == 1) {
            return new BigInteger("1");
        } else {
            return computeFibonacciBg(index - 1).add(computeFibonacciBg(index - 2));
        }
    }

    private void notifyResult(BigInteger result, Callback callback) {
        uiThreadPoster.post(() -> callback.onFibonacciComputed(result));
    }

    public interface Callback {

        void onFibonacciComputed(BigInteger result);
    }
}
