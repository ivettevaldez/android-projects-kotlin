package com.ivettevaldez.multithreading.demos.demorxjava;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

class ProducerConsumerBenchmarkUseCase {

    private final static int NUM_OF_MESSAGES = 1000;
    private final static int BLOCKING_QUEUE_CAPACITY = 5;

    private final MyBlockingQueue blockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);

    private long startTimestamp;

    Observable<Result> startBenchmark() {
        return Flowable.range(0, NUM_OF_MESSAGES)
                .flatMap(id -> Flowable
                        .fromCallable(() -> {
                                    blockingQueue.put(id);
                                    return id;
                                }
                        )
                        .subscribeOn(Schedulers.io())
                )
                .parallel(NUM_OF_MESSAGES)
                .runOn(Schedulers.io())
                .doOnNext(msg -> blockingQueue.take())
                .sequential()
                .count()
                .doOnSubscribe(s -> startTimestamp = System.currentTimeMillis())
                .map(count -> new Result(System.currentTimeMillis() - startTimestamp, count.intValue()))
                .toObservable();
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
