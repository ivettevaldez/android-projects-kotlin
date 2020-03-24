package com.ivettevaldez.multithreading.ui.main;

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
import com.ivettevaldez.multithreading.ui.exerciseone.ExerciseOneFragment;

public class MainFragment extends Fragment {

    private static String TAG = MainFragment.class.getSimpleName();

    private Button btnExercise1;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.main_fragment, container, false);

        initViews(layout);
        initListeners();

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            goToFragment(new ExerciseOneFragment());
        }
    };

    private void initViews(View layout) {
        btnExercise1 = layout.findViewById(R.id.main_button_ex_1);
    }

    private void initListeners() {
        btnExercise1.setOnClickListener(btnListener);
    }
}
