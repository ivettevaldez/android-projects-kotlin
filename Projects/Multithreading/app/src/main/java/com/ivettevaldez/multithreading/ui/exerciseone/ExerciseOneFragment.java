package com.ivettevaldez.multithreading.ui.exerciseone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseOneFragment extends Fragment {

    public ExerciseOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Exercise1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    private static ExerciseOneFragment newInstance(String param1, String param2) {
        ExerciseOneFragment fragment = new ExerciseOneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.exercise_one_fragment, container, false);
    }
}
