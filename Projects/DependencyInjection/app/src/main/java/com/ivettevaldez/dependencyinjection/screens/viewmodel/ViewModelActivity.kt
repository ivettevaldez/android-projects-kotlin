package com.ivettevaldez.dependencyinjection.screens.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ivettevaldez.dependencyinjection.R
import com.ivettevaldez.dependencyinjection.screens.common.controllers.BaseActivity
import com.ivettevaldez.dependencyinjection.screens.common.navigation.IScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.toolbar.MyToolbar
import javax.inject.Inject

class ViewModelActivity : BaseActivity() {

    @Inject
    lateinit var screensNavigator: IScreensNavigator

    private lateinit var toolbar: MyToolbar

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, ViewModelActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.layout_view_model)

        toolbar = findViewById(R.id.view_model_toolbar)
        toolbar.setNavigateUpListener {
            screensNavigator.navigateBack()
        }
    }
}