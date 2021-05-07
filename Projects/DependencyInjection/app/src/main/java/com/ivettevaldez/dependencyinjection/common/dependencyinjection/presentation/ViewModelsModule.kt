package com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation

import androidx.lifecycle.ViewModel
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.ViewModelKey
import com.ivettevaldez.dependencyinjection.screens.viewmodel.MyViewModel
import com.ivettevaldez.dependencyinjection.screens.viewmodel.MyViewModel2
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MyViewModel::class)
    abstract fun myViewModel(myViewModel: MyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyViewModel2::class)
    abstract fun myViewModel2(myViewModel2: MyViewModel2): ViewModel
}