package com.ivettevaldez.multithreading.demos.demothreadposter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;
import com.ivettevaldez.multithreading.common.BaseFragment;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemoThreadPosterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoThreadPosterFragment extends BaseFragment
        implements ProducerConsumerBenchmarkUseCase.Listener {

    private TextView textTime;
    private TextView textMessages;
    private ProgressBar progress;
    private Button buttonStart;

    private ProducerConsumerBenchmarkUseCase producerConsumerBenchmarkUseCase;

    public static DemoThreadPosterFragment newInstance() {
        return new DemoThreadPosterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        producerConsumerBenchmarkUseCase = new ProducerConsumerBenchmarkUseCase(
                getCompositionRoot().getUiThreadPoster(),
                getCompositionRoot().getBackgroundThreadPoster()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise5, container, false);

        initViews(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        producerConsumerBenchmarkUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        producerConsumerBenchmarkUseCase.unregisterListener(this);
    }

    @Override
    public void onBenchmarkCompleted(ProducerConsumerBenchmarkUseCase.Result result) {
        progress.setVisibility(View.INVISIBLE);
        buttonStart.setEnabled(true);

        textTime.setText(String.format(
                Locale.getDefault(),
                "Execution time: %d ms",
                result.getExecutionTime())
        );

        textMessages.setText(String.format(
                Locale.getDefault(),
                "Received messages: %d",
                result.getNumberOfMessages())
        );
    }

    private void initViews(View view) {
        textTime = view.findViewById(R.id.exercise5_text_time);
        textMessages = view.findViewById(R.id.exercise5_text_messages);
        progress = view.findViewById(R.id.exercise5_progress);

        buttonStart = view.findViewById(R.id.exercise5_button_start);
        buttonStart.setOnClickListener(view1 -> {
            buttonStart.setEnabled(false);
            textTime.setText("");
            textMessages.setText("");
            progress.setVisibility(View.VISIBLE);

            producerConsumerBenchmarkUseCase.startBenchmarkAndNotify();
        });
    }
}
