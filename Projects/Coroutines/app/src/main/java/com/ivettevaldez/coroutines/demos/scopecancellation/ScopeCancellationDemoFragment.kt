package com.ivettevaldez.coroutines.demos.scopecancellation

import android.os.Bundle
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
import kotlinx.coroutines.*
import java.util.*

class ScopeCancellationDemoFragment : BaseFragment() {

    override val screenTitle get() = ScreenReachableFromHome.DEMO_SCOPE_CANCELLATION.description

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private lateinit var textRemainingTime: TextView
    private lateinit var buttonStart: Button

    private var hasBenchmarkBeenStartedOnce: Boolean = false

    companion object {

        private const val SECOND: Long = 1_000_000_000L

        @JvmStatic
        fun newInstance(): Fragment = ScopeCancellationDemoFragment()
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

            val benchmarkDurationSeconds = 5

            coroutineScope.launch {
                updateRemainingTime(benchmarkDurationSeconds)
            }

            coroutineScope.launch {
                buttonStart.isEnabled = false

                val iterationsCount = executeBenchmark(benchmarkDurationSeconds)
                showIterationsCountMessage(iterationsCount)

                buttonStart.isEnabled = true
            }

            hasBenchmarkBeenStartedOnce = true
        }

        return view
    }

    override fun onStop() {
        logThreadInfo("onStop()")
        super.onStop()

        coroutineScope.cancel()

        if (hasBenchmarkBeenStartedOnce) {
            buttonStart.isEnabled = true
            textRemainingTime.text = getString(R.string.message_done)
        }
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

    private fun showIterationsCountMessage(iterationsCount: Long) {
        val message = String.format(
            Locale.getDefault(),
            getString(R.string.template_iterations_count),
            iterationsCount
        )
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private suspend fun updateRemainingTime(remainingSeconds: Int) {
        for (time in remainingSeconds downTo 0) {
            if (time > 0) {
                logThreadInfo("updateRemainingTime: $time seconds")

                textRemainingTime.text = String.format(
                    Locale.getDefault(),
                    getString(R.string.template_remaining_time),
                    time
                )

                delay(1000)
            } else {
                textRemainingTime.text = getString(R.string.message_done)
            }
        }
    }

    private suspend fun executeBenchmark(benchmarkDurationSeconds: Int): Long =
        withContext(Dispatchers.Default) {
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