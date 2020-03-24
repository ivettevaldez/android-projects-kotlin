package com.ivettevaldez.multithreading.ui.exercise1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Exercise1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Exercise1Fragment extends Fragment {

    private static final int ITERATIONS_COUNTER_DURATION_SEC = 10;
    private static final String TAG = Exercise1Fragment.class.getSimpleName();

    public Exercise1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Exercise1Fragment.
     */
    public static Exercise1Fragment newInstance() {
        Exercise1Fragment fragment = new Exercise1Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the view for this fragment
        View view = inflater.inflate(R.layout.exercise1_fragment, container, false);

        Button btnCountIterations = view.findViewById(R.id.exercise1_button_iterations);
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
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            countIterations();
        }
    };
}
