package com.ivettevaldez.multithreading.exercises;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Exercise3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Exercise3Fragment extends Fragment {

    private static final String TAG = Exercise3Fragment.class.getSimpleName();

    private static final int ONE_SECOND = 1000; // Milliseconds.
    private static final int SECONDS_TO_COUNT = 3;

    private Button btnCountSeconds;
    private TextView textCount;
    private Handler handler = new Handler(Looper.getMainLooper());

    public Exercise3Fragment() {
        // Required empty public constructor
    }

    public static Exercise3Fragment newInstance() {
        return new Exercise3Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise3, container, false);

        textCount = view.findViewById(R.id.exercise3_text_count);
        btnCountSeconds = view.findViewById(R.id.exercise3_button_count_seconds);

        btnCountSeconds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countIterations();
            }
        });

        return view;
    }

    private void updateViews(final String countState, final Boolean enableButton) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textCount.setText(countState);
                btnCountSeconds.setEnabled(enableButton);
            }
        });
    }

    private void countIterations() {
        /*
         * 1. Disable button to prevent multiple clicks.
         * 2. Start counting on background thread using loop and Thread.sleep().
         * 3. Show count in TextView.
         * 4. When count completes, show "done" in TextView and enable the button.
         */

        btnCountSeconds.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < SECONDS_TO_COUNT; i++) {
                    try {
                        updateViews(i + 1 + " s", false);
                        Thread.sleep(ONE_SECOND);
                    } catch (InterruptedException ex) {
                        Log.e(TAG, ex.toString());
                        return;
                    }
                }

                updateViews("Done", true);
            }
        }).start();
    }
}
