package com.ivettevaldez.unittesting.unittestinginandroid.example13

import android.text.TextUtils

class AndroidTestingProblems {

    fun callStaticAndroidApi(string: String) {
        TextUtils.isEmpty(string)
    }
}