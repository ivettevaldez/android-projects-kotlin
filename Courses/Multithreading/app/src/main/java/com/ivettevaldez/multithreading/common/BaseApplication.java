package com.ivettevaldez.multithreading.common;

import android.app.Application;

import com.ivettevaldez.multithreading.common.dependencyinjection.ApplicationCompositionRoot;

import static kotlinx.coroutines.DispatchersKt.IO_PARALLELISM_PROPERTY_NAME;

public class BaseApplication extends Application {

    private ApplicationCompositionRoot appCompositionRoot = new ApplicationCompositionRoot();

    public ApplicationCompositionRoot getAppCompositionRoot() {
        return appCompositionRoot;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.setProperty(IO_PARALLELISM_PROPERTY_NAME, String.valueOf(Integer.MAX_VALUE));
    }
}
