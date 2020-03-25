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
import com.ivettevaldez.multithreading.ui.exercise1.Exercise1Fragment;
import com.ivettevaldez.multithreading.ui.exercise2.Exercise2Fragment;

public class MainFragment extends Fragment {

    private static String TAG = MainFragment.class.getSimpleName();

    private Button btnExercise1;
    private Button btnExercise2;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        initViews(view);
        initListeners();

        return view;
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

    private Fragment getFragment(int viewId) {
        Fragment fragment;

        switch (viewId) {
            case R.id.main_button_ex_2:
                fragment = Exercise2Fragment.newInstance();
                break;

            default:
                fragment = Exercise1Fragment.newInstance();
                break;
        }

        return fragment;
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment fragment = getFragment(view.getId());
            goToFragment(fragment);
        }
    };

    private void initViews(View layout) {
        btnExercise1 = layout.findViewById(R.id.main_button_ex_1);
        btnExercise2 = layout.findViewById(R.id.main_button_ex_2);
    }

    private void initListeners() {
        btnExercise1.setOnClickListener(btnListener);
        btnExercise2.setOnClickListener(btnListener);
    }
}
