package com.ivettevaldez.multithreading.exercises;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

import java.math.BigInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Exercise4Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Exercise4Fragment extends Fragment {

    private final static int MAX_TIMEOUT_MS = 1000;
    private final static Object THREADS_COMPLETION_LOCK = new Object();

    private final String TAG = this.getClass().getSimpleName();
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    // UI Thread related
    private EditText editArgument;
    private EditText editTimeout;
    private Button buttonCalculate;
    private TextView textResult;

    private int numberOfThreads;
    private int numberOfFinishedThreads;
    private long computationTimeout;

    private ComputationRange[] threadsComputationRanges;
    private volatile BigInteger[] threadsComputationResults;

    private volatile boolean abortComputation;

    public Exercise4Fragment() {
        // Required empty public constructor
    }

    public static Exercise4Fragment newInstance() {
        return new Exercise4Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise4, container, false);

        initViews(view);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        abortComputation = true;
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) requireContext()
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(buttonCalculate.getWindowToken(), 0);
        }
    }

    private void initThreadsComputationRanges(int argument) {
        int computationRangeSize = argument / numberOfThreads;
        long nextComputationRangeEnd = argument;

        for (int i = numberOfThreads - 1; i >= 0; i--) {
            threadsComputationRanges[i] = new ComputationRange(
                    nextComputationRangeEnd - computationRangeSize + 1,
                    nextComputationRangeEnd
            );

            nextComputationRangeEnd = threadsComputationRanges[i].start - 1;
        }

        // Add potentially "remaining" values to the last thread's range.
        threadsComputationRanges[numberOfThreads - 1].end = argument;
    }

    private void initParams(int argument, long timeout) {
        numberOfThreads = argument < 20 ? 1 : Runtime.getRuntime().availableProcessors();

        synchronized (THREADS_COMPLETION_LOCK) {
            numberOfFinishedThreads = 0;
        }

        abortComputation = false;
        computationTimeout = System.currentTimeMillis() + timeout;
        threadsComputationResults = new BigInteger[numberOfThreads];
        threadsComputationRanges = new ComputationRange[numberOfThreads];

        initThreadsComputationRanges(argument);
    }

    private void startComputation() {
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    long rangeStart = threadsComputationRanges[threadIndex].start;
                    long rangeEnd = threadsComputationRanges[threadIndex].end;
                    BigInteger product = new BigInteger("1");

                    for (long num = rangeStart; num <= rangeEnd; num++) {
                        if (isTimeout()) break;
                        product = product.multiply(new BigInteger(String.valueOf(num)));
                    }

                    threadsComputationResults[threadIndex] = product;

                    synchronized (THREADS_COMPLETION_LOCK) {
                        numberOfFinishedThreads++;
                        THREADS_COMPLETION_LOCK.notifyAll();
                    }
                }
            }).start();
        }
    }

    private boolean isTimeout() {
        return System.currentTimeMillis() >= computationTimeout;
    }

    private long getRemainingMillisToTimeout() {
        return computationTimeout - System.currentTimeMillis();
    }

    @WorkerThread
    private void waitForThreadsResultsOrTimeoutOrAbort() {
        synchronized (THREADS_COMPLETION_LOCK) {
            while (numberOfFinishedThreads != numberOfThreads
                    && !abortComputation
                    && !isTimeout()) {
                try {
                    THREADS_COMPLETION_LOCK.wait(getRemainingMillisToTimeout());
                } catch (InterruptedException ex) {
                    Log.e(TAG, ex.toString());
                    return;
                }
            }
        }
    }

    private BigInteger computeFinalResult() {
        BigInteger result = new BigInteger("1");

        for (int i = 0; i < numberOfThreads; i++) {
            if (isTimeout()) break;
            result = result.multiply(threadsComputationResults[i]);
        }

        return result;
    }

    @WorkerThread
    private void processResults() {
        String strResult;

        if (abortComputation) {
            strResult = "Computation aborted";
        } else {
            strResult = computeFinalResult().toString();
        }

        // Need to check for timeout after the computation of the final result.
        if (isTimeout()) {
            strResult = "Computation timeout";
        }

        final String finalResult = strResult;

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!Exercise4Fragment.this.isStateSaved()) {
                    textResult.setText(finalResult);
                    buttonCalculate.setEnabled(true);
                }
            }
        });
    }

    private void computeFactorial(final int argument, final long timeout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initParams(argument, timeout);
                startComputation();
                waitForThreadsResultsOrTimeoutOrAbort();
                processResults();
            }
        }).start();
    }

    private long getTimeout() {
        int timeout;
        String strTimeout = editTimeout.getText().toString().trim();

        if (strTimeout.isEmpty()) {
            timeout = MAX_TIMEOUT_MS;
        } else {
            timeout = Integer.parseInt(strTimeout);
        }

        if (timeout > MAX_TIMEOUT_MS) {
            timeout = MAX_TIMEOUT_MS;
        }

        return timeout;
    }

    private void initViews(View view) {
        editArgument = view.findViewById(R.id.exercise4_edit_argument);
        editTimeout = view.findViewById(R.id.exercise4_edit_timeout);
        textResult = view.findViewById(R.id.exercise4_text_result);

        buttonCalculate = view.findViewById(R.id.exercise4_button_calculate);
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String argument = editArgument.getText().toString().trim();
                if (argument.isEmpty()) {
                    return;
                }

                textResult.setText("");
                buttonCalculate.setEnabled(false);

                hideKeyboard();
                computeFactorial(Integer.parseInt(argument), getTimeout());
            }
        });
    }

    private static class ComputationRange {

        private long start;
        private long end;

        ComputationRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
}
