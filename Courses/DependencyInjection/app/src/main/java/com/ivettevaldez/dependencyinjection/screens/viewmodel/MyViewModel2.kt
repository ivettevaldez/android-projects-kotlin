package com.ivettevaldez.dependencyinjection.screens.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.questions.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyViewModel2 @Inject constructor(
    private val fetchQuestionsUseCase: FetchQuestionsUseCase,
    private val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase
) : ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions
}