package com.ivettevaldez.multithreading.common;

import android.app.Application;

import com.ivettevaldez.multithreading.common.dependencyinjection.ApplicationCompositionRoot;

public class BaseApplication extends Application {

    private ApplicationCompositionRoot applicationCompositionRoot
            = new ApplicationCompositionRoot();

    public ApplicationCompositionRoot getApplicationCompositionRoot() {
        return applicationCompositionRoot;
    }
}
