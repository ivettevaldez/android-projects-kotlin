package com.ivettevaldez.dependencyinjection.screens.viewmodel

/* ktlint-disable no-wildcard-imports */

import androidx.lifecycle.*
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.questions.Question
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class MyViewModel(
    private val fetchQuestionsUseCase: FetchQuestionsUseCase
) : ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    init {

        viewModelScope.launch {

            val result = fetchQuestionsUseCase.execute()
            if (result is FetchQuestionsUseCase.Result.Success) {
                _questions.value = result.questions
            } else {
                throw RuntimeException("Fetch failed!")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val fetchQuestionsUseCase: Provider<FetchQuestionsUseCase>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MyViewModel(fetchQuestionsUseCase.get()) as T
        }
    }
}