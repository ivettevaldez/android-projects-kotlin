package com.ivettevaldez.multithreading.exercises.exercise6;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

import java.math.BigInteger;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Exercise6Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Exercise6Fragment extends Fragment {

    private final static int MAX_TIMEOUT_MS = 1000;

    private EditText editArgument;
    private EditText editTimeout;
    private Button buttonStartWork;
    private TextView textResult;

    private ComputeFactorialUseCase computeFactorialUseCase;

    private @Nullable
    Disposable disposable;

    public Exercise6Fragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new Exercise6Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        computeFactorialUseCase = new ComputeFactorialUseCase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise6, container, false);

        initViews(view);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void onFactorialComputed(BigInteger result) {
        showResult(String.format(Locale.getDefault(), result.toString()));
    }

    public void onFactorialComputationTimedOut() {
        showResult("Computation timed out");
    }

    public void onFactorialComputationAborted() {
        showResult("Computation aborted");
    }

    private void showResult(String result) {
        if (!Exercise6Fragment.this.isStateSaved()) {
            textResult.setText(result);
            buttonStartWork.setEnabled(true);
        }
    }

    private int getTimeout() {
        int timeout;
        if (editTimeout.getText().toString().isEmpty()) {
            timeout = MAX_TIMEOUT_MS;
        } else {
            timeout = Integer.parseInt(editTimeout.getText().toString());
            if (timeout > MAX_TIMEOUT_MS) {
                timeout = MAX_TIMEOUT_MS;
            }
        }
        return timeout;
    }

    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) requireContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(buttonStartWork.getWindowToken(), 0);
        }
    }

    private void initViews(View view) {
        editArgument = view.findViewById(R.id.exercise6_edit_argument);
        editTimeout = view.findViewById(R.id.exercise6_edit_timeout);
        textResult = view.findViewById(R.id.exercise6_text_result);

        buttonStartWork = view.findViewById(R.id.exercise6_button_calculate);
        buttonStartWork.setOnClickListener(view1 -> {
            String argument = editArgument.getText().toString().trim();
            if (argument.isEmpty()) {
                return;
            }

            textResult.setText("");
            buttonStartWork.setEnabled(false);

            hideKeyboard();

            disposable = computeFactorialUseCase.computeFactorial(Integer.parseInt(argument), getTimeout())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                                if (result.isAborted()) {
                                    onFactorialComputationAborted();
                                } else if (result.isTimedOut()) {
                                    onFactorialComputationTimedOut();
                                } else {
                                    onFactorialComputed(result.getResult());
                                }
                            }
                    );
        });
    }
}
