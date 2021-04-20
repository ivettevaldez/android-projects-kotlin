package com.ivettevaldez.cleanarchitecture.screens.common.main

import android.os.Bundle
import android.widget.FrameLayout
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseActivity
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IBackPressDispatcher
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.IBackPressedListener
import com.ivettevaldez.cleanarchitecture.screens.common.fragmentframehelper.IFragmentFrameWrapper
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreenNavigator
import kotlinx.android.synthetic.main.layout_frame_content.*

class MainActivity : BaseActivity(),
    IBackPressDispatcher,
    IFragmentFrameWrapper {

    private val backPressedListeners: MutableSet<IBackPressedListener> = HashSet()

    private lateinit var screenNavigator: ScreenNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_frame_content)

        screenNavigator = getCompositionRoot().getScreenNavigator()

        if (savedInstanceState == null) {
            screenNavigator.toQuestionsList()
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