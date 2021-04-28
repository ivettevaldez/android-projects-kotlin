package com.ivettevaldez.dependencyinjection.networking

import com.google.gson.annotations.SerializedName
import com.ivettevaldez.dependencyinjection.questions.Question

class QuestionsListResponseSchema(

    @SerializedName("items")
    val questions: List<Question>
)