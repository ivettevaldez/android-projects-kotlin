package com.ivettevaldez.cleanarchitecture.networking.users

import com.google.gson.annotations.SerializedName

data class UserSchema(

    @SerializedName("display_name")
    private val userDisplayName: String,

    @SerializedName("profile_image")
    private val userAvatarUrl: String
)
