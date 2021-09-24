package com.ivettevaldez.coroutines.demos.basiccoroutine

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ivettevaldez.coroutines.R
import com.ivettevaldez.coroutines.common.BaseFragment
import com.ivettevaldez.coroutines.common.ThreadInfoLogger
import com.ivettevaldez.coroutines.home.ScreenReachableFromHome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BasicCoroutineDemoFragment : BaseFragment() {

    override val screenTitle get() = ScreenReachableFromHome.DEMO_BASIC_COROUTINE.description

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private lateinit var textRemainingTime: TextView
    private lateinit var buttonStart: Button

    companion object {

        private const val SECOND: Long = 1_000_000_000L

        @JvmStatic
        fun newInstance(): Fragment = BasicCoroutineDemoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_basic_coroutine_demo, container, false)

        textRemainingTime = view.findViewById(R.id.basic_coroutine_text_remaining_time)

        buttonStart = view.findViewById(R.id.basic_coroutine_button_start)
        buttonStart.setOnClickListener {
            logThreadInfo("Button callback")

            coroutineScope.launch {
                buttonStart.isEnabled = false

                val iterationsCount = executeBenchmark()

                Toast.makeText(
                    requireContext(),
                    "Iterations: $iterationsCount",
                    Toast.LENGTH_SHORT
                ).show()

                buttonStart.isEnabled = true
            }
        }

        return view
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

    private fun updateRemainingTime(remainingSeconds: Int) {
        logThreadInfo("updateRemainingTime: $remainingSeconds seconds")

        if (remainingSeconds > 0) {
            textRemainingTime.text = "$remainingSeconds seconds remaining"

            Handler(Looper.getMainLooper()).postDelayed({
                updateRemainingTime(remainingSeconds - 1)
            }, 1000)
        } else {
            textRemainingTime.text = "Done!"
        }
    }

    private suspend fun executeBenchmark(): Long {
        val benchmarkDurationSeconds = 5

        updateRemainingTime(benchmarkDurationSeconds)

        return withContext(Dispatchers.Default) {
            logThreadInfo("Benchmark started")

            val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * SECOND

            var iterationsCount: Long = 0
            while (System.nanoTime() < stopTimeNano) {
                iterationsCount++
            }

            logThreadInfo("Benchmark completed")

            iterationsCount
        }
    }
}