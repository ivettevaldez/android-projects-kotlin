package com.ivettevaldez.multithreading.exercises;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Exercise1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Exercise1Fragment extends Fragment {

    private static final String TAG = Exercise1Fragment.class.getSimpleName();
    private static final int ITERATIONS_COUNTER_DURATION_SEC = 10;

    private Button btnCountIterations;
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            countIterations();
        }
    };

    public Exercise1Fragment() {
        // Required empty public constructor
    }

    public static Exercise1Fragment newInstance() {
        return new Exercise1Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the view for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise1, container, false);

        btnCountIterations = view.findViewById(R.id.exercise1_button_iterations);
        btnCountIterations.setOnClickListener(buttonListener);

        return view;
    }

    private void countIterations() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long startTimestamp = System.currentTimeMillis();
                long endTimestamp = startTimestamp + ITERATIONS_COUNTER_DURATION_SEC * 1000;

                int iterationsCount = 0;
                while (System.currentTimeMillis() <= endTimestamp) {
                    iterationsCount++;
                }

                Log.d(
                        TAG,
                        String.format("Iterations in %d seconds: %d",
                                ITERATIONS_COUNTER_DURATION_SEC, iterationsCount)
                );

                final int iterationsCountFinal = iterationsCount;

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // UI changes can only be made on the Main Thread.
                        btnCountIterations.setText(
                                String.format(
                                        Locale.getDefault(),
                                        "Iterations: %d",
                                        iterationsCountFinal
                                )
                        );
                    }
                });
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
