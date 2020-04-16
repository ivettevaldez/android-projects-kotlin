package com.ivettevaldez.multithreading.demos.democoroutines

import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

private const val NUM_OF_MESSAGES = 1000
private const val BLOCKING_QUEUE_CAPACITY = 5

class ProducerConsumerBenchmarkUseCase {

    private val classTag: String = this.javaClass.simpleName
    private val blockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private var numOfReceivedMessages: AtomicInteger = AtomicInteger(0)
    private var numOfConsumers: AtomicInteger = AtomicInteger(0)

    @Volatile
    private var startTimestamp: Long = 0

    class Result(val executionTime: Long, val numberOfMessages: Int)

    suspend fun startBenchmark(): Result {
        withContext(Dispatchers.IO) {
            numOfReceivedMessages.set(0)
            startTimestamp = System.currentTimeMillis()

            // Producers init coroutine.
            val deferredProducers = async(Dispatchers.IO + NonCancellable) {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewProducer(i)
                }
            }

            // Consumers init coroutine.
            val deferredConsumers = async(Dispatchers.IO + NonCancellable) {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewConsumer()
                }
            }

            awaitAll(deferredProducers, deferredConsumers)
        }

        return Result(
                System.currentTimeMillis() - startTimestamp,
                numOfReceivedMessages.get()
        )
    }

    private fun CoroutineScope.startNewProducer(i: Int) = launch(Dispatchers.IO) {
        Log.d(classTag, "Producer $i started on Thread ${Thread.currentThread().name}")
        blockingQueue.put(i)
    }

    private fun CoroutineScope.startNewConsumer() = launch(Dispatchers.IO) {
        val message = blockingQueue.take()
        if (message != -1) {
            numOfReceivedMessages.incrementAndGet()
        }
        Log.d(classTag,
                "Consumer ${numOfConsumers.incrementAndGet()} " +
                        "started on Thread ${Thread.currentThread().name}"
        )
    }
}
