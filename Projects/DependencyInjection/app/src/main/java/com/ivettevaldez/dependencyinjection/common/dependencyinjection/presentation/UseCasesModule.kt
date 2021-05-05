package com.ivettevaldez.dependencyinjection.common.dependencyinjection.presentation

import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun fetchQuestionsUseCase(stackOverflowApi: StackOverflowApi) =
        FetchQuestionsUseCase(stackOverflowApi)

    @Provides
    fun fetchQuestionDetailsUseCase(stackOverflowApi: StackOverflowApi) =
        FetchQuestionDetailsUseCase(stackOverflowApi)
}