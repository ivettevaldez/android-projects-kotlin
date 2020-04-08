package com.ivettevaldez.multithreading.demos.demorxjava;

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;

class ProducerConsumerBenchmarkUseCase {

    private final static int NUM_OF_MESSAGES = 1000;
    private final static int BLOCKING_QUEUE_CAPACITY = 5;
    private final static Object LOCK = new Object();

    private final String classTag = this.getClass().getSimpleName();
    private final MyBlockingQueue blockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);
    private final AtomicInteger numOfThreads = new AtomicInteger(0);

    private int numOfReceivedMessages;
    private int numOfFinishedConsumers;
    private long startTimestamp;

    Observable<Result> startBenchmark() {
        return Observable.fromCallable(
                () -> {
                    synchronized (LOCK) {
                        numOfReceivedMessages = 0;
                        numOfFinishedConsumers = 0;
                        numOfThreads.set(0);
                        startTimestamp = System.currentTimeMillis();
                    }

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

                    synchronized (LOCK) {
                        while (numOfFinishedConsumers < NUM_OF_MESSAGES) {
                            try {
                                LOCK.wait();
                            } catch (InterruptedException ex) {
                                Log.e(classTag, ex.toString());
                                return new Result(
                                        System.currentTimeMillis() - startTimestamp,
                                        -1
                                );
                            }
                        }

                        return new Result(
                                System.currentTimeMillis() - startTimestamp,
                                numOfReceivedMessages
                        );
                    }
                }
        );
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
}
