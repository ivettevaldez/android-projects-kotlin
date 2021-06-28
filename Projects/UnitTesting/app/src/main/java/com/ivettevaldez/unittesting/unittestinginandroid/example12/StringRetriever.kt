package com.ivettevaldez.unittesting.unittestinginandroid.example12

import android.content.Context

class StringRetriever(private val context: Context) {

    fun getString(id: Int): String = context.getString(id)
}