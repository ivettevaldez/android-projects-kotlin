package com.ivettevaldez.unittesting.tutorialandroidapp.screens.questiondetails

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ivettevaldez.unittesting.R
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.BaseViewMvc
import com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.views.ViewMvc

interface QuestionDetailsViewMvc : ViewMvc

class QuestionDetailsViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : BaseViewMvc(),
    QuestionDetailsViewMvc {

    init {

        setRootView(
            inflater.inflate(R.layout.layout_question_details, parent, false)
        )
    }
}