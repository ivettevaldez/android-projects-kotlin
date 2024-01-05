package com.ivettevaldez.dependencyinjection.screens.common.viewsmvc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.dependencyinjection.screens.common.images.ImageLoader
import com.ivettevaldez.dependencyinjection.screens.questiondetails.QuestionDetailsViewMvcImpl
import com.ivettevaldez.dependencyinjection.screens.questionslist.QuestionsListViewMvcImpl
import javax.inject.Inject
import javax.inject.Provider

class ViewMvcFactory @Inject constructor(
    private val inflater: Provider<LayoutInflater>,
    private val imageLoader: Provider<ImageLoader>
) {

    fun newQuestionsListViewMvc(parent: ViewGroup?): QuestionsListViewMvcImpl {
        return QuestionsListViewMvcImpl(inflater.get(), parent)
    }

    fun newQuestionDetailsViewMvc(parent: ViewGroup?): QuestionDetailsViewMvcImpl {
        return QuestionDetailsViewMvcImpl(inflater.get(), parent, imageLoader.get())
    }
}