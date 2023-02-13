package com.ivettevaldez.coroutines.demos.coroutinescancellationcooperative2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ivettevaldez.coroutines.R
import com.ivettevaldez.coroutines.common.BaseFragment
import com.ivettevaldez.coroutines.home.ScreenReachableFromHome
import kotlinx.coroutines.*
import com.ivettevaldez.coroutines.common.ThreadInfoLogger as logger

class CoroutinesCancellationCooperative2DemoFragment : BaseFragment() {

    override val screenTitle: String
        get() = ScreenReachableFromHome.COROUTINES_CANCELLATION_COOPERATIVE_2_DEMO.description

    private lateinit var benchmarkUseCase: CancellableBenchmark2UseCase

    private lateinit var btnStart: Button
    private lateinit var txtRemainingTime: TextView

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    companion object {

        private const val BENCHMARK_DURATION_SECONDS: Int = 5
        private const val ONE_SECOND_MILLIS: Long = 1000L

        fun newInstance() = CoroutinesCancellationCooperative2DemoFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        benchmarkUseCase = compositionRoot.cancellableBenchmark2UseCase
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loop_iterations_demo, container, false)

        txtRemainingTime = view.findViewById(R.id.txt_remaining_time)

        btnStart = view.findViewById(R.id.btn_start)
        btnStart.setOnClickListener {
            logger.logThreadInfo("button callback")

            coroutineScope.launch {
                // This must be executed on the Main thread
                updateRemainingTime()
            }

            coroutineScope.launch(Dispatchers.Default) {
                try {
                    // This must be executed on a Background thread
                    val iterationsCount =
                        benchmarkUseCase.executeBenchmark(BENCHMARK_DURATION_SECONDS)
                    logger.logThreadInfo("Iterations: $iterationsCount")
                } catch (ex: CancellationException) {
                    logger.logThreadInfo(ex.message.toString())
                }
            }
        }

        return view
    }

    override fun onStop() {
        logger.logThreadInfo("onStop()")
        super.onStop()
        // Cancel coroutines
        coroutineScope.coroutineContext.cancelChildren()
    }

    private suspend fun updateRemainingTime() {
        for (time in BENCHMARK_DURATION_SECONDS downTo 0) {
            if (time > 0) {
                logger.logThreadInfo("updateRemainingTime(): $time seconds")
                txtRemainingTime.text = getString(R.string.seconds_remaining_template, time)
                delay(ONE_SECOND_MILLIS)
            } else {
                txtRemainingTime.text = getString(R.string.done)
            }
        }
    }
}