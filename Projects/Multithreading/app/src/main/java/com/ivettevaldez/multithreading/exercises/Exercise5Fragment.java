package com.ivettevaldez.multithreading.exercises;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Exercise5Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Exercise5Fragment extends Fragment {

    private final static int NUM_OF_MESSAGES = 1000;
    private final static int BLOCKING_QUEUE_CAPACITY = 5;

    private final static Object LOCK = new Object();

    private final String TAG = this.getClass().getSimpleName();
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final MyBlockingQueue blockingQueue = new MyBlockingQueue(BLOCKING_QUEUE_CAPACITY);

    private int numOfReceivedMessages;
    private int numOfFinishedConsumers;
    private long startTimestamp;

    // UI Thread related.
    private TextView textTime;
    private TextView textMessages;
    private ProgressBar progress;
    private Button buttonStart;

    public static Exercise5Fragment newInstance() {
        return new Exercise5Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise5, container, false);

        textTime = view.findViewById(R.id.exercise5_text_time);
        textMessages = view.findViewById(R.id.exercise5_text_messages);
        progress = view.findViewById(R.id.exercise5_progress);

        buttonStart = view.findViewById(R.id.exercise5_button_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStart.setEnabled(false);
                textTime.setText("");
                textMessages.setText("");
                progress.setVisibility(View.VISIBLE);

                numOfReceivedMessages = 0;
                numOfFinishedConsumers = 0;

                startCommunication();
            }
        });

        return view;
    }

    private void startCommunication() {
        startTimestamp = System.currentTimeMillis();

        // Watcher-reporter thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
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

                showResults();
            }
        }).start();

        // Producers init thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                    startNewProducer(i);
                }
            }
        }).start();

        // Consumers init thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < NUM_OF_MESSAGES; i++) {
                    startNewConsumer();
                }
            }
        }).start();
    }

    private void startNewProducer(final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                blockingQueue.put(index);
            }
        }).start();
    }

    private void startNewConsumer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int message = blockingQueue.take();

                synchronized (LOCK) {
                    if (message != -1) {
                        numOfReceivedMessages++;
                    }
                    numOfFinishedConsumers++;
                    LOCK.notifyAll();
                }
            }
        }).start();
    }

    private void showResults() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.INVISIBLE);
                buttonStart.setEnabled(true);

                long executionTime = System.currentTimeMillis() - startTimestamp;
                textTime.setText(String.format(
                        Locale.getDefault(),
                        "Execution time: %d ms",
                        executionTime)
                );

                synchronized (LOCK) {
                    textMessages.setText(String.format(
                            Locale.getDefault(),
                            "Received messages: %d",
                            numOfReceivedMessages)
                    );
                }
            }
        });
    }

    /**
     * Simplified implementation of BlockingQueue.
     */
    private static class MyBlockingQueue {

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
        private void put(int number) {
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
        private int take() {
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
}
