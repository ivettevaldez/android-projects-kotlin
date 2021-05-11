package com.ivettevaldez.saturnus.screens.common.main

import android.os.Bundle
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.screens.common.controllers.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}