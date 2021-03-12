package com.ivettevaldez.cleanarchitecture.networking.questions

import com.google.gson.annotations.SerializedName
import com.ivettevaldez.cleanarchitecture.networking.users.UserSchema

data class QuestionSchema(

    @SerializedName("title")
    val title: String,

    @SerializedName("question_id")
    val id: String,

    @SerializedName("body")
    val body: String,

    @SerializedName("owner")
    val owner: UserSchema
)
