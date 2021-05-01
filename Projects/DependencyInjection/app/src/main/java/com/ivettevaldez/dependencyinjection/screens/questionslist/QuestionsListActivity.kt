package com.ivettevaldez.dependencyinjection.screens.questionslist

import android.os.Bundle
import com.ivettevaldez.dependencyinjection.R
import com.ivettevaldez.dependencyinjection.screens.common.controllers.BaseActivity

class QuestionsListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_container)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_container, QuestionsListFragment.newInstance(), null)
                .commitAllowingStateLoss()
        }
    }
}