package com.ivettevaldez.multithreading.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ivettevaldez.multithreading.R;
import com.ivettevaldez.multithreading.demos.DemoAtomicityFragment;
import com.ivettevaldez.multithreading.demos.DemoCustomHandlerFragment;
import com.ivettevaldez.multithreading.demos.DemoUiThreadFragment;
import com.ivettevaldez.multithreading.demos.demoasynctask.DemoAsyncTaskFragment;
import com.ivettevaldez.multithreading.demos.demorxjava.DemoRxJavaFragment;
import com.ivettevaldez.multithreading.demos.demothreadposter.DemoThreadPosterFragment;
import com.ivettevaldez.multithreading.exercises.exercise1.Exercise1Fragment;
import com.ivettevaldez.multithreading.exercises.exercise2.Exercise2Fragment;
import com.ivettevaldez.multithreading.exercises.exercise3.Exercise3Fragment;
import com.ivettevaldez.multithreading.exercises.exercise4.Exercise4Fragment;
import com.ivettevaldez.multithreading.exercises.exercise5.Exercise5Fragment;
import com.ivettevaldez.multithreading.exercises.exercise6.Exercise6Fragment;

public class MainFragment extends Fragment {

    private static String TAG = MainFragment.class.getSimpleName();

    private Button btnExercise1;
    private Button btnExercise2;
    private Button btnExercise3;
    private Button btnExercise4;
    private Button btnExercise5;
    private Button btnExercise6;
    private Button btnDemoUiThread;
    private Button btnDemoCustomHandler;
    private Button btnDemoAtomicity;
    private Button btnDemoAsyncTask;
    private Button btnDemoThreadPoster;
    private Button btnDemoRxJava;

    private View.OnClickListener listener = view -> {
        Fragment fragment = getFragment(view.getId());
        goToFragment(fragment);
    };

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initViews(view);
        initListeners();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews(View layout) {
        btnExercise1 = layout.findViewById(R.id.main_button_exercise1);
        btnExercise2 = layout.findViewById(R.id.main_button_exercise2);
        btnExercise3 = layout.findViewById(R.id.main_button_exercise3);
        btnExercise4 = layout.findViewById(R.id.main_button_exercise4);
        btnExercise5 = layout.findViewById(R.id.main_button_exercise5);
        btnExercise6 = layout.findViewById(R.id.main_button_exercise6);
        btnDemoUiThread = layout.findViewById(R.id.main_button_demo_ui_thread);
        btnDemoCustomHandler = layout.findViewById(R.id.main_button_demo_custom_handler);
        btnDemoAtomicity = layout.findViewById(R.id.main_button_demo_atomicity);
        btnDemoAsyncTask = layout.findViewById(R.id.main_button_demo_async_task);
        btnDemoThreadPoster = layout.findViewById(R.id.main_button_demo_thread_poster);
        btnDemoRxJava = layout.findViewById(R.id.main_button_demo_rx_java);
    }

    private void initListeners() {
        btnExercise1.setOnClickListener(listener);
        btnExercise2.setOnClickListener(listener);
        btnExercise3.setOnClickListener(listener);
        btnExercise4.setOnClickListener(listener);
        btnExercise5.setOnClickListener(listener);
        btnExercise6.setOnClickListener(listener);
        btnDemoUiThread.setOnClickListener(listener);
        btnDemoCustomHandler.setOnClickListener(listener);
        btnDemoAtomicity.setOnClickListener(listener);
        btnDemoAsyncTask.setOnClickListener(listener);
        btnDemoThreadPoster.setOnClickListener(listener);
        btnDemoRxJava.setOnClickListener(listener);
    }

    private Fragment getFragment(int viewId) {
        Fragment fragment;

        switch (viewId) {
            case R.id.main_button_exercise2:
                fragment = Exercise2Fragment.newInstance();
                break;
            case R.id.main_button_demo_ui_thread:
                fragment = DemoUiThreadFragment.newInstance();
                break;
            case R.id.main_button_demo_custom_handler:
                fragment = DemoCustomHandlerFragment.newInstance();
                break;
            case R.id.main_button_exercise3:
                fragment = Exercise3Fragment.newInstance();
                break;
            case R.id.main_button_demo_atomicity:
                fragment = DemoAtomicityFragment.newInstance();
                break;
            case R.id.main_button_exercise4:
                fragment = Exercise4Fragment.newInstance();
                break;
            case R.id.main_button_exercise5:
                fragment = Exercise5Fragment.newInstance();
                break;
            case R.id.main_button_exercise6:
                fragment = Exercise6Fragment.newInstance();
                break;
            case R.id.main_button_demo_async_task:
                fragment = DemoAsyncTaskFragment.newInstance();
                break;
            case R.id.main_button_demo_thread_poster:
                fragment = DemoThreadPosterFragment.newInstance();
                break;
            case R.id.main_button_demo_rx_java:
                fragment = DemoRxJavaFragment.newInstance();
                break;
            default:
                fragment = Exercise1Fragment.newInstance();
                break;
        }

        return fragment;
    }

    private void goToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        if (getActivity() != null && fragmentManager != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        } else {
            Log.e(TAG, "NULL Activity or FragmentManager");
        }
    }
}
