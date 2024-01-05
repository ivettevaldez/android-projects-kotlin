package com.ivettevaldez.dependencyinjection.networking

import com.google.gson.annotations.SerializedName
import com.ivettevaldez.dependencyinjection.questions.QuestionWithBody

data class SingleQuestionResponseSchema(
    @SerializedName("items")
    val questions: List<QuestionWithBody>
) {
    val question: QuestionWithBody get() = questions[0]
}