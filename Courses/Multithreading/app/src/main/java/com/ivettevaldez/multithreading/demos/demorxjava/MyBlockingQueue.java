package com.ivettevaldez.multithreading.demos.demorxjava;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Simplified implementation of BlockingQueue.
 */
class MyBlockingQueue {

    private final String TAG = this.getClass().getSimpleName();

    private final Object QUEUE_LOCK = new Object();
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    private int currentSize = 0;

    MyBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Inserts the specified element into the queue,
     * waiting if necessary for space to become available.
     *
     * @param number the element to add.
     */
    void put(int number) {
        synchronized (QUEUE_LOCK) {
            while (currentSize >= capacity) {
                try {
                    QUEUE_LOCK.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, e.toString());
                    return;
                }
            }

            queue.offer(number);
            currentSize++;
            QUEUE_LOCK.notifyAll();
        }
    }

    /**
     * Retrieves and removes the head of the queue,
     * waiting if necessary until an element becomes available.
     *
     * @return the head of the queue.
     */
    int take() {
        synchronized (QUEUE_LOCK) {
            while (currentSize <= 0) {
                try {
                    QUEUE_LOCK.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, e.toString());
                    return 0;
                }
            }

            currentSize--;
            QUEUE_LOCK.notifyAll();
            return queue.poll();
        }
    }
}
