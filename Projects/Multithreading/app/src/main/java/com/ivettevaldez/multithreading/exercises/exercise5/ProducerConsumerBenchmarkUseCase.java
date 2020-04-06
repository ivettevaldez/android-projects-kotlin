package com.ivettevaldez.multithreading.exercises.exercise5;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ivettevaldez.multithreading.common.BaseObservable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final static Handler UI_HANDLER = new Handler(Looper.getMainLooper());

    private final String classTag = this.getClass().getSimpleName();
    private final MyBlockingQueue blockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);
    private final AtomicInteger numOfThreads = new AtomicInteger(0);
    private final ExecutorService threadPool = Executors.newCachedThreadPool(
            runnable -> {
                Log.d(classTag, "Thread: " + numOfThreads.incrementAndGet());
                return new Thread(runnable);
            }
    );

    private int numOfReceivedMessages;
    private int numOfFinishedConsumers;
    private long startTimestamp;

    void startBenchmarkAndNotify() {
        synchronized (LOCK) {
            numOfReceivedMessages = 0;
            numOfFinishedConsumers = 0;
            numOfThreads.set(0);
            startTimestamp = System.currentTimeMillis();
        }

        // Watcher-reporter thread.
        threadPool.execute(() -> {
            synchronized (LOCK) {
                while (numOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException ex) {
                        Log.e(classTag, ex.toString());
                        return;
                    }
                }
            }

            notifySuccess();
        });

        // Producers init thread.
        threadPool.execute(() -> {
            for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewProducer(i);
            }
        });

        // Consumers init thread.
        threadPool.execute(() -> {
            for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                startNewConsumer();
            }
        });
    }

    private void notifySuccess() {
        UI_HANDLER.post(() -> {
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
        threadPool.execute(() -> blockingQueue.put(index));
    }

    private void startNewConsumer() {
        threadPool.execute(() -> {
            int message = blockingQueue.take();

            synchronized (LOCK) {
                if (message != -1) {
                    numOfReceivedMessages++;
                }
                numOfFinishedConsumers++;
                LOCK.notifyAll();
            }
        });
    }
}
