package com.ivettevaldez.multithreading.exercises.exercise5;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ivettevaldez.multithreading.common.BaseObservable;

class ProducerConsumerBenchmarkUseCase extends BaseObservable<ProducerConsumerBenchmarkUseCase.Listener> {

    public interface Listener {

        void onBenchmarkCompleted(Result result);
    }

    static class Result {

        private final long executionTime;
        private final int numberOfMessages;

        Result(long executionTime, int numberOfMessages) {
            this.executionTime = executionTime;
            this.numberOfMessages = numberOfMessages;
        }

        long getExecutionTime() {
            return executionTime;
        }

        int getNumberOfMessages() {
            return numberOfMessages;
        }
    }

    private final static int NUM_OF_MESSAGES = 1000;
    private final static int BLOCKING_QUEUE_CAPACITY = 5;

    private final static Object LOCK = new Object();

    private final MyBlockingQueue blockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final String TAG = this.getClass().getSimpleName();

    private int numOfReceivedMessages;
    private int numOfFinishedConsumers;
    private long startTimestamp;

    void startBenchmarkAndNotify() {
        synchronized (LOCK) {
            numOfReceivedMessages = 0;
            numOfFinishedConsumers = 0;
            startTimestamp = System.currentTimeMillis();
        }

        // Watcher-reporter thread.
        new Thread(() -> {
            synchronized (LOCK) {
                while (numOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException ex) {
                        Log.e(TAG, ex.toString());
                        return;
                    }
                }
            }

            notifySuccess();
        }).start();

        // Producers init thread.
        new Thread(() -> {
            for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewProducer(i);
            }
        }).start();

        // Consumers init thread.
        new Thread(() -> {
            for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewConsumer();
            }
        }).start();
    }

    private void notifySuccess() {
        uiHandler.post(() -> {
            Result result;

            synchronized (LOCK) {
                long executionTime = System.currentTimeMillis() - startTimestamp;
                result = new Result(executionTime, numOfReceivedMessages);
            }

            for (Listener listener : getListeners()) {
                listener.onBenchmarkCompleted(result);
            }
        });
    }

    private void startNewProducer(final int index) {
        new Thread(() -> blockingQueue.put(index)).start();
    }

    private void startNewConsumer() {
        new Thread(() -> {
            int message = blockingQueue.take();

            synchronized (LOCK) {
                if (message != -1) {
                    numOfReceivedMessages++;
                }
                numOfFinishedConsumers++;
                LOCK.notifyAll();
            }
        }).start();
    }
}
