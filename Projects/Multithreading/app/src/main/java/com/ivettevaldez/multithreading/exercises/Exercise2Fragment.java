package com.ivettevaldez.multithreading.exercises;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Exercise2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Exercise2Fragment extends Fragment {

    private static final String TAG = Exercise2Fragment.class.getSimpleName();

    private byte[] dummyData;
    private boolean fragmentAlive = true;

    public Exercise2Fragment() {
        // Required empty public constructor
    }

    public static Exercise2Fragment newInstance() {
        return new Exercise2Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dummyData = new byte[100 * 1000 * 1000];
        return inflater.inflate(R.layout.exercise2_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        countScreenTime();
    }

    @Override
    public void onStop() {
        fragmentAlive = false;
        dummyData = null;

        super.onStop();
    }

    private void countScreenTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int screenTimeSeconds = 0;

                while (fragmentAlive) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        return;
                    }

                    screenTimeSeconds++;
                    Log.d(TAG, String.format("Screen time: %d s", screenTimeSeconds));
                }
            }
        }).start();
    }
}
