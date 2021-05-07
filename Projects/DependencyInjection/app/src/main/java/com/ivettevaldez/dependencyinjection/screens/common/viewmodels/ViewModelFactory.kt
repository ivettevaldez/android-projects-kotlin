package com.ivettevaldez.dependencyinjection.screens.common.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivettevaldez.dependencyinjection.screens.viewmodel.MyViewModel
import com.ivettevaldez.dependencyinjection.screens.viewmodel.MyViewModel2
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val viewModelProvider: Provider<MyViewModel>,
    private val viewModel2Provider: Provider<MyViewModel2>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MyViewModel::class.java -> viewModelProvider.get() as T
            MyViewModel2::class.java -> viewModel2Provider.get() as T
            else -> throw RuntimeException("Unsupported ViewModel type: $modelClass")
        }
    }
}