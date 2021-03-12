package com.ivettevaldez.cleanarchitecture.networking.questions

import com.google.gson.annotations.SerializedName

data class QuestionsListResponseSchema(

    @SerializedName("items")
    var questions: List<QuestionSchema>?
)