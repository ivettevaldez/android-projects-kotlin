package com.ivettevaldez.multithreading.demos.democoroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private const val NUM_OF_MESSAGES = 1000
private const val BLOCKING_QUEUE_CAPACITY = 5

class ProducerConsumerBenchmarkUseCase {

    private val reentrantLock = ReentrantLock()
    private val lockCondition = reentrantLock.newCondition()

    private val blockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private var numOfReceivedMessages: Int = 0
    private var numOfFinishedConsumers: Int = 0
    private var startTimestamp: Long = 0

    class Result(val executionTime: Long, val numberOfMessages: Int)

    suspend fun startBenchmark(): Result {
        withContext(Dispatchers.IO) {
            reentrantLock.withLock {
                numOfReceivedMessages = 0
                numOfFinishedConsumers = 0
                startTimestamp = System.currentTimeMillis()
            }

            // Producers thread.
            Thread {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewProducer(i)
                }
            }.start()

            // Consumers thread.
            Thread {
                for (i in 0 until NUM_OF_MESSAGES) {
                    startNewConsumer()
                }
            }.start()

            reentrantLock.withLock {
                while (numOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        lockCondition.await()
                    } catch (ex: InterruptedException) {
                        return@withLock
                    }
                }
            }
        }

        reentrantLock.withLock {
            return Result(
                    System.currentTimeMillis() - startTimestamp,
                    numOfReceivedMessages
            )
        }
    }

    private fun startNewProducer(i: Int) {
        Thread { blockingQueue.put(i) }.start()
    }

    private fun startNewConsumer() {
        Thread {
            val message = blockingQueue.take()

            reentrantLock.withLock {
                if (message != -1) {
                    numOfReceivedMessages++
                }
                numOfFinishedConsumers++
                lockCondition.signalAll()
            }
        }.start()
    }
}