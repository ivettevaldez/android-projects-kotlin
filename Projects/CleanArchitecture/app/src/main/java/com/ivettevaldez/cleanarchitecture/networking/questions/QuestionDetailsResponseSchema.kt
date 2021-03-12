package com.ivettevaldez.cleanarchitecture.networking.questions

/* ktlint-disable no-wildcard-imports */

import com.google.gson.annotations.SerializedName
import java.util.*

class QuestionDetailsResponseSchema(question: QuestionSchema?) {

    @SerializedName("items")
    private var questions: List<QuestionSchema>? =
        Collections.singletonList(question) as List<QuestionSchema>?

    fun getQuestion(): QuestionSchema {
        return questions!![0]
    }
}