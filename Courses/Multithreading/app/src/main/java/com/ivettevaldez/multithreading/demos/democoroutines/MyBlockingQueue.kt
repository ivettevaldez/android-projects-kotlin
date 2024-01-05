package com.ivettevaldez.multithreading.demos.democoroutines

import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class MyBlockingQueue(private val capacity: Int) {

    private val reentrantLock = ReentrantLock()
    private val lockCondition = reentrantLock.newCondition()

    private val queue: Queue<Int> = LinkedList()

    private var currentSize = 0

    /**
     * Inserts the specified element into the queue,
     * waiting if necessary for space to become available.
     *
     * @param number the element to add.
     */
    fun put(number: Int) {
        reentrantLock.withLock {
            while (currentSize >= capacity) {
                try {
                    lockCondition.await()
                } catch (e: InterruptedException) {
                    return
                }
            }

            queue.offer(number)
            currentSize++
            lockCondition.signalAll()
        }
    }

    /**
     * Retrieves and removes the head of the queue,
     * waiting if necessary until an element becomes available.
     *
     * @return the head of the queue.
     */
    fun take(): Int {
        reentrantLock.withLock {
            while (currentSize <= 0) {
                try {
                    lockCondition.await()
                } catch (e: InterruptedException) {
                    return 0
                }
            }
            currentSize--
            lockCondition.signalAll()
            return queue.poll()!!
        }
    }
}