package com.ivettevaldez.multithreading.demos;

public class DemoVisibility {

    private static final Object LOCK = new Object();

    private static volatile int count = 0;

    public static void main(String[] args) {
        new Consumer().start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            return;
        }

        Thread producer = new Producer();
        producer.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            return;
        }

        producer.interrupt();
    }

    static class Consumer extends Thread {
        @Override
        public void run() {
            int localValue = -1;

            while (true) {
                synchronized (LOCK) {
                    if (localValue != count) {
                        System.out.println("Consumer: detected count change " + count);
                        localValue = count;
                    }

                    if (count >= 5) break;
                }
            }

            System.out.println("Consumer: terminating");
        }
    }

    static class Producer extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (LOCK) {
                    if (count >= 5) break;

                    int localValue = count;
                    localValue++;

                    System.out.println("Producer: incrementing count to " + localValue);

                    count = localValue;
                }

                if (isInterrupted()) {
                    System.out.println("Producer: interrupted flag was set");
                    return;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Producer: interrupted during sleep");
                    return;
                }
            }

            System.out.println("Producer: terminating");
        }
    }
}
