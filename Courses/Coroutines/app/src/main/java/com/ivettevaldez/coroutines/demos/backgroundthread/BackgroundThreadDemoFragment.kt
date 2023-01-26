package com.ivettevaldez.coroutines.demos.backgroundthread

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
import com.ivettevaldez.coroutines.common.ThreadInfoLogger as logger

class BackgroundThreadDemoFragment : BaseFragment() {

    override val screenTitle: String
        get() = ScreenReachableFromHome.BACKGROUND_THREAD_DEMO.description

    private lateinit var btnStart: Button
    private lateinit var txtRemainingTime: TextView

    companion object {

        private const val BENCHMARK_DURATION_SECONDS: Int = 5
        private const val VALUE_SECOND: Long = 1_000_000_000L

        fun newInstance() = BackgroundThreadDemoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loop_iterations_demo, container, false)

        txtRemainingTime = view.findViewById(R.id.txt_remaining_time)

        btnStart = view.findViewById(R.id.btn_start)
        btnStart.setOnClickListener {
            logger.logThreadInfo("button callback")
            it.isEnabled = false
            executeBenchmark()
            it.isEnabled = true
        }

        return view
    }

    private fun updateRemainingTime(remainingTimeSeconds: Int) {
        logger.logThreadInfo("updateRemainingTime(): $remainingTimeSeconds seconds")

        if (remainingTimeSeconds > 0) {
            txtRemainingTime.text = getString(
                R.string.seconds_remaining_template,
                remainingTimeSeconds
            )
            Handler(Looper.getMainLooper()).postDelayed({
                updateRemainingTime(remainingTimeSeconds - 1)
            }, 1000)
        } else {
            txtRemainingTime.text = getString(R.string.done)
        }
    }

    private fun executeBenchmark() {
        logger.logThreadInfo("benchmark started")
        updateRemainingTime(BENCHMARK_DURATION_SECONDS)

        Thread {
            val stopTimeNano = System.nanoTime() + BENCHMARK_DURATION_SECONDS * VALUE_SECOND

            var iterationsCount: Long = 0
            while (System.nanoTime() < stopTimeNano) {
                iterationsCount++
            }

            logger.logThreadInfo("benchmark completed")

            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    requireContext(), "Iterations: $iterationsCount", Toast.LENGTH_SHORT
                ).show()
            }
        }.start()
    }
}