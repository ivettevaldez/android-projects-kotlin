package com.ivettevaldez.coroutines

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.coroutines.common.ScreensNavigator
import com.ivettevaldez.coroutines.common.ToolbarDelegate
import com.ivettevaldez.coroutines.common.dependencyinjection.ActivityCompositionRoot

class MainActivity : AppCompatActivity(), ToolbarDelegate {

    val compositionRoot: ActivityCompositionRoot by lazy {
        ActivityCompositionRoot(
            this,
            (application as CustomApplication).applicationCompositionRoot
        )
    }

    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var buttonBack: ImageButton
    private lateinit var textScreenTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screensNavigator = compositionRoot.screensNavigator
        screensNavigator.init(savedInstanceState)

        buttonBack = findViewById(R.id.btn_back)
        buttonBack.setOnClickListener { screensNavigator.navigateUp() }

        textScreenTitle = findViewById(R.id.txt_screen_title)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        screensNavigator.onSaveInstanceState(outState)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!screensNavigator.navigateBack()) {
            super.onBackPressed()
        }
    }

    override fun setScreenTitle(title: String) {
        textScreenTitle.text = title
    }

    override fun showUpButton() {
        buttonBack.visibility = View.VISIBLE
    }

    override fun hideUpButton() {
        buttonBack.visibility = View.INVISIBLE
    }
}