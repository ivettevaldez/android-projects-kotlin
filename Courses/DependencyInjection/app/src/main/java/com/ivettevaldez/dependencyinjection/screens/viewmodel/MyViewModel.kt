package com.ivettevaldez.dependencyinjection.screens.viewmodel

/* ktlint-disable no-wildcard-imports */

import androidx.lifecycle.*
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.questions.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val fetchQuestionsUseCase: FetchQuestionsUseCase,
    private val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _questions: MutableLiveData<List<Question>> =
        savedStateHandle.getLiveData("questions")

    var questions: LiveData<List<Question>> = _questions

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
}