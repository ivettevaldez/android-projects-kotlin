package com.ivettevaldez.coroutines.demos.scopechildrencancellation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.ivettevaldez.coroutines.R
import com.ivettevaldez.coroutines.common.BaseFragment
import com.ivettevaldez.coroutines.home.ScreenReachableFromHome
import kotlinx.coroutines.*
import com.ivettevaldez.coroutines.common.ThreadInfoLogger as logger

class ScopeChildrenCancellationDemoFragment : BaseFragment() {

    override val screenTitle: String
        get() = ScreenReachableFromHome.SCOPE_CHILDREN_CANCELLATION_DEMO.description

    private lateinit var btnStart: Button
    private lateinit var txtRemainingTime: TextView

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private var hasBenchmarkBeenStartedOnce: Boolean = false

    companion object {

        private const val BENCHMARK_DURATION_SECONDS: Int = 5
        private const val ONE_SECOND_NANO: Long = 1_000_000_000L
        private const val ONE_SECOND_MILLIS: Long = 1000L

        fun newInstance() = ScopeChildrenCancellationDemoFragment()
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

            coroutineScope.launch {
                // This must be executed on the Main thread
                btnStart.isEnabled = false

                // This must be executed on a Background thread
                val iterationsCount = executeBenchmark()

                // This must be executed on the Main thread
                Toast.makeText(
                    requireContext(), "Iterations: $iterationsCount", Toast.LENGTH_SHORT
                ).show()
                btnStart.isEnabled = true
            }

            hasBenchmarkBeenStartedOnce = true
        }

        return view
    }

    override fun onStop() {
        logger.logThreadInfo("onStop()")
        super.onStop()
        // Cancel coroutines
        coroutineScope.coroutineContext.cancelChildren()
        if (hasBenchmarkBeenStartedOnce) {
            // Reset UI state
            btnStart.isEnabled = true
            txtRemainingTime.text = getString(R.string.done)
        }
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

    private suspend fun executeBenchmark(): Long = withContext(Dispatchers.Default) {
        logger.logThreadInfo("benchmark started")
        val stopTimeNano = System.nanoTime() + BENCHMARK_DURATION_SECONDS * ONE_SECOND_NANO

        var iterationsCount: Long = 0
        while (System.nanoTime() < stopTimeNano) {
            iterationsCount++
        }
        logger.logThreadInfo("benchmark completed")

        iterationsCount
    }
}