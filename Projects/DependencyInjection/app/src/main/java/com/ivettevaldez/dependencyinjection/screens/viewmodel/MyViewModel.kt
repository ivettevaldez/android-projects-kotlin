package com.ivettevaldez.dependencyinjection.screens.viewmodel

/* ktlint-disable no-wildcard-imports */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.questions.Question
import com.ivettevaldez.dependencyinjection.screens.common.viewmodels.SavedStateViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyViewModel @Inject constructor(
    private val fetchQuestionsUseCase: FetchQuestionsUseCase,
    private val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase
) : SavedStateViewModel() {

    private lateinit var _questions: MutableLiveData<List<Question>>

    lateinit var questions: LiveData<List<Question>>

    override fun init(savedStateHandle: SavedStateHandle) {
        _questions = savedStateHandle.getLiveData("questions")
        questions = _questions

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