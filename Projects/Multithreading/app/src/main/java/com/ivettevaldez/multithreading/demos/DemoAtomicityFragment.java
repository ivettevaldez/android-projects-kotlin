package com.ivettevaldez.multithreading.demos;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemoAtomicityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoAtomicityFragment extends Fragment {

    private static final int COUNT_UP_TO = 1000;
    private static final int NUM_OF_COUNTER_THREADS = 100;
    private final AtomicInteger count = new AtomicInteger(0);
    private Button btnStartCount;
    private TextView textFinalCount;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public DemoAtomicityFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new DemoAtomicityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo_atomicity, container, false);

        textFinalCount = view.findViewById(R.id.demo_atomicity_text_final_count);

        btnStartCount = view.findViewById(R.id.demo_atomicity_button_start_count);
        btnStartCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCount();
            }
        });

        return view;
    }

    private void startCount() {
        count.set(0);
        textFinalCount.setText("");
        btnStartCount.setEnabled(false);

        for (int i = 0; i < NUM_OF_COUNTER_THREADS; i++) {
            startCountThread();
        }

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textFinalCount.setText(String.valueOf(count.get()));
                btnStartCount.setEnabled(true);
            }
        }, NUM_OF_COUNTER_THREADS * 20);
    }

    private void startCountThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < COUNT_UP_TO; i++) {
                    count.incrementAndGet();
                }
            }
        }).start();
    }
}
