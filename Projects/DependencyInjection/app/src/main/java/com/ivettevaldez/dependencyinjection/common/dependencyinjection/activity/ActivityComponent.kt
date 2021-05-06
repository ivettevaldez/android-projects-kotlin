package com.ivettevaldez.dependencyinjection.common.dependencyinjection.activity

import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation.PresentationComponent
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun newPresentationComponent(): PresentationComponent

    @Subcomponent.Builder
    interface Builder {

        fun activityModule(activityModule: ActivityModule): Builder

        @BindsInstance
        fun activity(activity: AppCompatActivity): Builder
        
        fun build(): ActivityComponent
    }
}