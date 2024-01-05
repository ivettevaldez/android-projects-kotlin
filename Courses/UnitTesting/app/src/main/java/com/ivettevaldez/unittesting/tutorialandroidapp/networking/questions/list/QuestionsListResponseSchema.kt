package com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.list

import com.google.gson.annotations.SerializedName
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.QuestionSchema

data class QuestionsListResponseSchema(

    @SerializedName("items")
    val questions: List<QuestionSchema>
)
