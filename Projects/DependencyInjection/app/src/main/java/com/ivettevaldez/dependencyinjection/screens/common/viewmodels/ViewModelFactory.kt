package com.ivettevaldez.dependencyinjection.screens.common.viewmodels

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>,
    savedStateRegistryOwner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, null) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val provider = providers[modelClass]
        val viewModel = provider?.get() as T
            ?: throw RuntimeException("Unsupported ViewModel type: $modelClass")

        if (viewModel is SavedStateViewModel) {
            viewModel.init(handle)
        }

        return viewModel
    }
}