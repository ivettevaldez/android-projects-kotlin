package com.ivettevaldez.multithreading.common;

import androidx.fragment.app.Fragment;

import com.ivettevaldez.multithreading.common.dependencyinjection.ApplicationCompositionRoot;

public class BaseFragment extends Fragment {

    private ApplicationCompositionRoot applicationCompositionRoot;

    protected final ApplicationCompositionRoot getCompositionRoot() {
        if (applicationCompositionRoot == null) {
            applicationCompositionRoot = ((BaseApplication) requireActivity().getApplication())
                    .getApplicationCompositionRoot();
        }
        return applicationCompositionRoot;
    }
}
