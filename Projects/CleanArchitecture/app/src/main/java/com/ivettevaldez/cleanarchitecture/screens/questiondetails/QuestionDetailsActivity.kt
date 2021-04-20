package com.ivettevaldez.cleanarchitecture.screens.questiondetails

import android.os.Bundle
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseActivity
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IBackPressedListener
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreenNavigator

class QuestionDetailsActivity : BaseActivity() {

    private lateinit var backPressedListener: IBackPressedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_frame_content)

        val fragment: QuestionDetailsFragment?
        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragment = QuestionDetailsFragment.newInstance(getQuestionId())
            fragmentTransaction.add(R.id.frame_content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(
                R.id.frame_content
            ) as QuestionDetailsFragment
        }

        backPressedListener = fragment
    }

    override fun onBackPressed() {
        if (!backPressedListener.onBackPressed()) {
            super.onBackPressed()
        }
    }

    private fun getQuestionId(): String {
        intent.extras?.takeIf { it.containsKey(ScreenNavigator.EXTRA_QUESTION_ID) }?.apply {
            return getString(ScreenNavigator.EXTRA_QUESTION_ID) ?: ""
        }
        return ""
    }
}