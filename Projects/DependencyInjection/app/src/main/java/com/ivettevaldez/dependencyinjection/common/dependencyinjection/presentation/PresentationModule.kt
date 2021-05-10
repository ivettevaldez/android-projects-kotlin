package com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation

import androidx.savedstate.SavedStateRegistryOwner
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val savedStateRegistryOwner: SavedStateRegistryOwner) {

    @Provides
    fun savedStateRegistryOwner(): SavedStateRegistryOwner = savedStateRegistryOwner
}