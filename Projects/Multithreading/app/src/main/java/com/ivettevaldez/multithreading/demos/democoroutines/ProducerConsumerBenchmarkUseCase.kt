package com.ivettevaldez.multithreading.demos.democoroutines

import android.os.Handler
import android.os.Looper
import com.ivettevaldez.multithreading.common.BaseObservable
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private const val NUM_OF_MESSAGES = 1000
private const val BLOCKING_QUEUE_CAPACITY = 5

class ProducerConsumerBenchmarkUseCase : BaseObservable<ProducerConsumerBenchmarkUseCase.Listener>() {

    private val reentrantLock = ReentrantLock()
    private val lockCondition = reentrantLock.newCondition()

    private val uiHandler = Handler(Looper.getMainLooper())
    private val blockingQueue = MyBlockingQueue(BLOCKING_QUEUE_CAPACITY)

    private var numOfReceivedMessages: Int = 0
    private var numOfFinishedConsumers: Int = 0
    private var startTimestamp: Long = 0

    interface Listener {

        fun onBenchmarkCompleted(result: Result?)
    }

    class Result(val executionTime: Long, val numberOfMessages: Int)

    fun startBenchmarkAndNotify() {
        reentrantLock.withLock {
            numOfReceivedMessages = 0
            numOfFinishedConsumers = 0
            startTimestamp = System.currentTimeMillis()
        }

        // Watcher/reporter thread.
        Thread {
            reentrantLock.withLock {
                while (numOfFinishedConsumers < NUM_OF_MESSAGES) {
                    try {
                        lockCondition.await()
                    } catch (ex: InterruptedException) {
                        return@Thread
                    }
                }
            }

            notifySuccess()
        }.start()

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
    }

    private fun notifySuccess() {
        uiHandler.post {
            lateinit var result: Result

            reentrantLock.withLock {
                result = Result(
                        System.currentTimeMillis() - startTimestamp,
                        numOfReceivedMessages
                )
            }
            
            for (listener in listeners) {
                listener.onBenchmarkCompleted(result)
            }
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