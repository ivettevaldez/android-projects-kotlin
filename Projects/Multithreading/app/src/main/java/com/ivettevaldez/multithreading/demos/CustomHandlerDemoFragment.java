package com.ivettevaldez.multithreading.demos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomHandlerDemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomHandlerDemoFragment extends Fragment {

    private static final String TAG = CustomHandlerDemoFragment.class.getSimpleName();

    private static final int ONE_SECOND = 1000; // Milliseconds.
    private static final int SECONDS_TO_COUNT = 5;

    private CustomHandler customHandler;

    public CustomHandlerDemoFragment() {
        // Required empty public constructor
    }

    public static CustomHandlerDemoFragment newInstance() {
        return new CustomHandlerDemoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.custom_handler_demo_fragment, container, false);

        Button btnSendJob = view.findViewById(R.id.custom_handler_demo_button);
        btnSendJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addJob();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        customHandler = new CustomHandler();
    }

    @Override
    public void onStop() {
        super.onStop();
        customHandler.stop();
    }

    private void addJob() {
        customHandler.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < SECONDS_TO_COUNT; i++) {
                    try {
                        Thread.sleep(ONE_SECOND);
                    } catch (InterruptedException e) {
                        return;
                    }

                    Log.d(TAG, String.format("Iteration: %d", i + 1));
                }
            }
        });
    }

    private static class CustomHandler {

        private final Runnable POISON = new Runnable() {
            @Override
            public void run() {
            }
        };

        private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

        CustomHandler() {
            initWorkerThread();
        }

        private void initWorkerThread() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Runnable runnable;

                    try {
                        runnable = queue.take();
                    } catch (InterruptedException e) {
                        return;
                    }

                    if (runnable == POISON) {
                        return;
                    }

                    runnable.run();
                }
            }).start();
        }

        private void stop() {
            queue.clear();
            queue.add(POISON);
        }

        private void post(Runnable job) {
            queue.add(job);
        }
    }
}
