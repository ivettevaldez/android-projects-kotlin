package com.ivettevaldez.cleanarchitecture.screens.common.main

import android.os.Bundle
import android.widget.FrameLayout
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseActivity
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IBackPressDispatcher
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IBackPressedListener
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IFragmentFrameWrapper
import com.ivettevaldez.cleanarchitecture.screens.questionslist.QuestionsListFragment
import kotlinx.android.synthetic.main.layout_frame_content.*

class MainActivity : BaseActivity(),
    IBackPressDispatcher,
    IFragmentFrameWrapper {

    private val backPressedListeners: MutableSet<IBackPressedListener> = HashSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_frame_content)

        val fragment: QuestionsListFragment?
        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragment = QuestionsListFragment()
            fragmentTransaction.add(R.id.frame_content, fragment).commit()
        }
    }

    override fun onBackPressed() {
        var isBackPressConsumedByAnyListener = false
        for (listener in backPressedListeners) {
            if (listener.onBackPressed()) {
                isBackPressConsumedByAnyListener = true
            }
        }

        if (!isBackPressConsumedByAnyListener) {
            super.onBackPressed()
        }
    }

    override fun registerListener(listener: IBackPressedListener) {
        backPressedListeners.add(listener)
    }

    override fun unregisterListener(listener: IBackPressedListener) {
        backPressedListeners.remove(listener)
    }

    override fun getFragmentFrame(): FrameLayout {
        return frame_content
    }
}