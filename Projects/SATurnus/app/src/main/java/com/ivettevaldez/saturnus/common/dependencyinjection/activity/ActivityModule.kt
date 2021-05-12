package com.ivettevaldez.saturnus.common.dependencyinjection.activity

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    companion object {

        @Provides
        fun layoutInflater(activity: AppCompatActivity): LayoutInflater =
            LayoutInflater.from(activity)

        @Provides
        fun fragmentManager(activity: AppCompatActivity): FragmentManager =
            activity.supportFragmentManager
    }
}