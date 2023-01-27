package com.ivettevaldez.coroutines.demos.coroutinescancellation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class CoroutinesCancellationDemoFragment : BaseFragment() {

    override val screenTitle: String
        get() = ScreenReachableFromHome.COROUTINES_CANCELLATION_DEMO.description

    private lateinit var btnStart: Button
    private lateinit var txtRemainingTime: TextView

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private var job: Job? = null

    companion object {

        private const val BENCHMARK_DURATION_SECONDS: Int = 5
        private const val VALUE_SECOND: Long = 1_000_000_000L

        fun newInstance() = CoroutinesCancellationDemoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loop_iterations_demo, container, false)

        txtRemainingTime = view.findViewById(R.id.txt_remaining_time)

        btnStart = view.findViewById(R.id.btn_start)
        btnStart.setOnClickListener {
            logger.logThreadInfo("button callback")

            job = coroutineScope.launch {
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
        }

        return view
    }

    override fun onStop() {
        logger.logThreadInfo("onStop()")
        super.onStop()
        // Cancel coroutine
        job?.cancel()
        // Reset UI state
        btnStart.isEnabled = true
        txtRemainingTime.visibility = View.INVISIBLE
    }

    private fun updateRemainingTime(remainingTimeSeconds: Int) {
        logger.logThreadInfo("updateRemainingTime(): $remainingTimeSeconds seconds")

        if (remainingTimeSeconds > 0) {
            txtRemainingTime.text = getString(
                R.string.seconds_remaining_template, remainingTimeSeconds
            )
            Handler(Looper.getMainLooper()).postDelayed({
                updateRemainingTime(remainingTimeSeconds - 1)
            }, 1000)
        } else {
            txtRemainingTime.text = getString(R.string.done)
        }
    }

    private suspend fun executeBenchmark(): Long {
        // This must be executed on the Main thread
        updateRemainingTime(BENCHMARK_DURATION_SECONDS)

        return withContext(Dispatchers.Default) {
            logger.logThreadInfo("benchmark started")
            val stopTimeNano = System.nanoTime() + BENCHMARK_DURATION_SECONDS * VALUE_SECOND

            var iterationsCount: Long = 0
            while (System.nanoTime() < stopTimeNano) {
                iterationsCount++
            }
            logger.logThreadInfo("benchmark completed")

            iterationsCount
        }
    }
}