package com.ivettevaldez.multithreading.exercises.exercise4;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

import java.math.BigInteger;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Exercise4Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Exercise4Fragment extends Fragment implements ComputeFactorialUseCase.Listener {

    private EditText editArgument;
    private EditText editTimeout;
    private Button buttonCalculate;
    private TextView textResult;

    private ComputeFactorialUseCase computeFactorialUseCase;

    public Exercise4Fragment() {
        // Required empty public constructor
    }

    public static Exercise4Fragment newInstance() {
        return new Exercise4Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        computeFactorialUseCase = new ComputeFactorialUseCase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise4, container, false);

        initViews(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        computeFactorialUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        computeFactorialUseCase.unregisterListener(this);
    }

    @Override
    public void onFactorialComputed(BigInteger result) {
        showResult(String.format(Locale.getDefault(), result.toString()));
    }

    @Override
    public void onFactorialComputationTimedOut() {
        showResult("Computation timed out");

    }

    @Override
    public void onFactorialComputationAborted() {
        showResult("Computation aborted");

    }

    private void showResult(String result) {
        if (!Exercise4Fragment.this.isStateSaved()) {
            textResult.setText(result);
            buttonCalculate.setEnabled(true);
        }
    }

    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) requireContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(buttonCalculate.getWindowToken(), 0);
        }
    }

    private void initViews(View view) {
        editArgument = view.findViewById(R.id.exercise4_edit_argument);
        editTimeout = view.findViewById(R.id.exercise4_edit_timeout);
        textResult = view.findViewById(R.id.exercise4_text_result);

        buttonCalculate = view.findViewById(R.id.exercise4_button_calculate);
        buttonCalculate.setOnClickListener(view1 -> {
            String argument = editArgument.getText().toString().trim();
            if (argument.isEmpty()) {
                return;
            }

            textResult.setText("");
            buttonCalculate.setEnabled(false);

            hideKeyboard();

            String strTimeout = editTimeout.getText().toString().trim();
            computeFactorialUseCase.computeFactorialAndNotify(Integer.parseInt(argument), strTimeout);
        });
    }
}
