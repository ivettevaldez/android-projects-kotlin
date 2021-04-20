package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.os.Bundle
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseActivity
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IBackPressedListener

class QuestionsListActivity : BaseActivity() {

    private lateinit var backPressedListener: IBackPressedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_frame_content)

        val fragment: QuestionsListFragment?
        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragment = QuestionsListFragment()
            fragmentTransaction.add(R.id.frame_content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(
                R.id.frame_content
            ) as QuestionsListFragment
        }

        backPressedListener = fragment
    }

    override fun onBackPressed() {
        if (!backPressedListener.onBackPressed()) {
            super.onBackPressed()
        }
    }
}