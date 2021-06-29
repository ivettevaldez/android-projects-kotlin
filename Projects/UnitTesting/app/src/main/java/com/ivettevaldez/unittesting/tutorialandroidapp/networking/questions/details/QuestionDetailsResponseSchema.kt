package com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details

import com.google.gson.annotations.SerializedName
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.QuestionSchema
import java.util.*

data class QuestionDetailsResponseSchema(

    @SerializedName("items")
    private var questions: MutableList<QuestionSchema>
) {

    fun questionDetailsResponseSchema(question: QuestionSchema?) {
        questions = Collections.singletonList(question)
    }

    fun getQuestion(): QuestionSchema {
        return questions[0]
    }
}
