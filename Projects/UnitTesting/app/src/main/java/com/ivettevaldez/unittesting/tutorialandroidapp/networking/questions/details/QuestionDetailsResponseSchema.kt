package com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details

import com.google.gson.annotations.SerializedName

data class QuestionDetailsResponseSchema(

    @SerializedName("items")
    val items: List<QuestionDetailsSchema>
)
