package com.ivettevaldez.multithreading.common.dependencyinjection;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.util.Locale;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ApplicationCompositionRoot {

    private final String classTag = this.getClass().getSimpleName();

    private ThreadPoolExecutor threadPoolExecutor;

    public Handler getUiHandler() {
        return new Handler(Looper.getMainLooper());
    }

    public UiThreadPoster getUiThreadPoster() {
        return new UiThreadPoster();
    }

    public BackgroundThreadPoster getBackgroundThreadPoster() {
        return new BackgroundThreadPoster();
    }

    public ThreadPoolExecutor getThreadPool() {
//    private final ExecutorService threadPool = Executors.newCachedThreadPool(
//            runnable -> {
//                Log.d(classTag, "Thread: " + numOfThreads.incrementAndGet());
//                return new Thread(runnable);
//            }
//    );
//    private final ExecutorService threadPool = Executors.newFixedThreadPool(1000);

        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(
                    10,
                    Integer.MAX_VALUE,
                    10L,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<>(),
                    runnable -> {
                        Log.d(classTag,
                                String.format(
                                        Locale.getDefault(),
                                        "Size: %s - Active count: %s - Remaining capacity: %s",
                                        threadPoolExecutor.getPoolSize(),
                                        threadPoolExecutor.getActiveCount(),
                                        threadPoolExecutor.getQueue().remainingCapacity()
                                )
                        );
                        return new Thread(runnable);
                    }
            );
        }
        return threadPoolExecutor;
    }
}
