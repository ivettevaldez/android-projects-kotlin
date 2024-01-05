package com.ivettevaldez.dependencyinjection.screens.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ivettevaldez.dependencyinjection.R
import com.ivettevaldez.dependencyinjection.screens.common.controllers.BaseActivity
import com.ivettevaldez.dependencyinjection.screens.common.navigation.IScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.toolbar.MyToolbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewModelActivity : BaseActivity() {

    @Inject
    lateinit var screensNavigator: IScreensNavigator

    private lateinit var viewModel: MyViewModel
    private lateinit var viewModel2: MyViewModel2

    private lateinit var toolbar: MyToolbar

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, ViewModelActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_view_model)

        toolbar = findViewById(R.id.view_model_toolbar)
        toolbar.setNavigateUpListener {
            screensNavigator.navigateBack()
        }

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        viewModel2 = ViewModelProvider(this).get(MyViewModel2::class.java)

        viewModel.questions.observe(this, { questions ->
            Toast.makeText(this, "Fetched ${questions.size}", Toast.LENGTH_SHORT).show()
        })
    }
}