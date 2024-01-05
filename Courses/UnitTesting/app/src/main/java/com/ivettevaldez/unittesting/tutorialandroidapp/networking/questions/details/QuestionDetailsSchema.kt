package com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details

import com.google.gson.annotations.SerializedName

data class QuestionDetailsSchema(

    @SerializedName("question_id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String
)
