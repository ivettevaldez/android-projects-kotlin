package com.ivettevaldez.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ivettevaldez.coroutines.common.ToolbarDelegate
import com.ivettevaldez.coroutines.common.dependencyinjection.ActivityCompositionRoot

class MainActivity : AppCompatActivity(), ToolbarDelegate {

    val compositionRoot: ActivityCompositionRoot by lazy {
        ActivityCompositionRoot(this)
    }


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}