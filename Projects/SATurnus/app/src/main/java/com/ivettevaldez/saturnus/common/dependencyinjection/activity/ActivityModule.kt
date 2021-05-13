package com.ivettevaldez.saturnus.common.dependencyinjection.activity

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ivettevaldez.saturnus.screens.common.fragmentframehelper.IFragmentFrameWrapper
import com.ivettevaldez.saturnus.screens.common.navigation.INavDrawerHelper
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

        @Provides
        fun fragmentFrameWrapper(activity: AppCompatActivity): IFragmentFrameWrapper =
            activity as IFragmentFrameWrapper

        @Provides
        fun navDrawerHelper(activity: AppCompatActivity): INavDrawerHelper =
            activity as INavDrawerHelper
    }
}