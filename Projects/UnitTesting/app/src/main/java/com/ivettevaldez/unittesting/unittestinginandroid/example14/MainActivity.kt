package com.ivettevaldez.unittesting.unittestinginandroid.example14

import android.app.Activity
import android.os.Bundle
import com.ivettevaldez.unittesting.R

class MainActivity : Activity() {

    var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start()
    }

    fun start() {
        count++
    }
}