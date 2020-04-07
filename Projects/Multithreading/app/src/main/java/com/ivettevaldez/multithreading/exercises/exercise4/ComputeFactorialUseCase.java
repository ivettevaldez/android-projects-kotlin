package com.ivettevaldez.multithreading.exercises.exercise4;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.ivettevaldez.multithreading.common.BaseObservable;

import java.math.BigInteger;
import java.util.concurrent.ThreadPoolExecutor;

class ComputeFactorialUseCase extends BaseObservable<ComputeFactorialUseCase.Listener> {

    private final static int MAX_TIMEOUT_MS = 1000;
    private final static Object THREADS_COMPLETION_LOCK = new Object();

    private final String TAG = this.getClass().getSimpleName();

    private final Handler uiHandler;
    private final ThreadPoolExecutor threadPool;

    private int numberOfThreads;
    private int numberOfFinishedThreads;
    private long computationTimeout;

    private ComputationRange[] threadsComputationRanges;
    private volatile BigInteger[] threadsComputationResults;

    private boolean abortComputation;

    ComputeFactorialUseCase(Handler uiHandler, ThreadPoolExecutor threadPool) {
        this.uiHandler = uiHandler;
        this.threadPool = threadPool;
    }

    @Override
    protected void onLastListenerUnregistered() {
        super.onLastListenerUnregistered();
        synchronized (THREADS_COMPLETION_LOCK) {
            abortComputation = true;
            THREADS_COMPLETION_LOCK.notifyAll();
        }
    }

    void computeFactorialAndNotify(final int argument, final String timeout) {
        threadPool.execute(() -> {
            initParams(argument, validateTimeout(timeout));
            startComputation();
            waitForThreadsResultsOrTimeoutOrAbort();
            processResults();
        });
    }

    private long validateTimeout(String strTimeout) {
        int timeout;

        if (strTimeout.isEmpty()) {
            timeout = MAX_TIMEOUT_MS;
        } else {
            timeout = Integer.parseInt(strTimeout);
        }

        if (timeout > MAX_TIMEOUT_MS) {
            timeout = MAX_TIMEOUT_MS;
        }

        return timeout;
    }

    private void initParams(int argument, long timeout) {
        numberOfThreads = argument < 20 ? 1 : Runtime.getRuntime().availableProcessors();

        synchronized (THREADS_COMPLETION_LOCK) {
            numberOfFinishedThreads = 0;
            abortComputation = false;
        }

        computationTimeout = System.currentTimeMillis() + timeout;
        threadsComputationResults = new BigInteger[numberOfThreads];
        threadsComputationRanges = new ComputationRange[numberOfThreads];

        initThreadsComputationRanges(argument);
    }

    private void initThreadsComputationRanges(int argument) {
        int computationRangeSize = argument / numberOfThreads;
        long nextComputationRangeEnd = argument;

        for (int i = numberOfThreads - 1; i >= 0; i--) {
            threadsComputationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            );

            nextComputationRangeEnd = threadsComputationRanges[i].start - 1;
        }

        // Add potentially "remaining" values to the last thread's range.
        threadsComputationRanges[numberOfThreads - 1].end = argument;
    }

    private void startComputation() {
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;

            threadPool.execute(() -> {
                long rangeStart = threadsComputationRanges[threadIndex].start;
                long rangeEnd = threadsComputationRanges[threadIndex].end;
                BigInteger product = new BigInteger("1");

                for (long num = rangeStart; num <= rangeEnd; num++) {
                    if (isTimeout()) break;
                    product = product.multiply(new BigInteger(String.valueOf(num)));
                }

                threadsComputationResults[threadIndex] = product;

                synchronized (THREADS_COMPLETION_LOCK) {
                    numberOfFinishedThreads++;
                    THREADS_COMPLETION_LOCK.notifyAll();
                }
            });
        }
    }

    private boolean isTimeout() {
        return System.currentTimeMillis() >= computationTimeout;
    }

    private long getRemainingMillisToTimeout() {
        return computationTimeout - System.currentTimeMillis();
    }

    @WorkerThread
    private void waitForThreadsResultsOrTimeoutOrAbort() {
        synchronized (THREADS_COMPLETION_LOCK) {
            while (numberOfFinishedThreads != numberOfThreads
                    && !abortComputation
                    && !isTimeout()) {
                try {
                    THREADS_COMPLETION_LOCK.wait(getRemainingMillisToTimeout());
                } catch (InterruptedException ex) {
                    Log.e(TAG, ex.toString());
                    return;
                }
            }
        }
    }

    @WorkerThread
    private void processResults() {
        if (abortComputation) {
            notifyAborted();
            return;
        }

        BigInteger result = computeFinalResult();

        // Need to check for timeout after the computation of the final result.
        if (isTimeout()) {
            notifyTimedOut();
            return;
        }

        notifySuccess(result);
    }

    private BigInteger computeFinalResult() {
        BigInteger result = new BigInteger("1");

        for (int i = 0; i < numberOfThreads; i++) {
            if (isTimeout()) break;
            result = result.multiply(threadsComputationResults[i]);
        }

        return result;
    }

    private void notifySuccess(BigInteger result) {
        uiHandler.post(() -> {
            for (Listener listener : getListeners()) {
                listener.onFactorialComputed(result);
            }
        });
    }

    private void notifyTimedOut() {
        uiHandler.post(() -> {
            for (Listener listener : getListeners()) {
                listener.onFactorialComputationTimedOut();
            }
        });
    }

    private void notifyAborted() {
        uiHandler.post(() -> {
            for (Listener listener : getListeners()) {
                listener.onFactorialComputationAborted();
            }
        });
    }

    public interface Listener {

        void onFactorialComputed(BigInteger result);

        void onFactorialComputationTimedOut();

        void onFactorialComputationAborted();
    }
}
