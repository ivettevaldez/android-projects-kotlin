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
import kotlinx.coroutines.*
import java.util.*

class BasicCoroutineDemoFragment : BaseFragment() {

    override val screenTitle get() = ScreenReachableFromHome.DEMO_BASIC_COROUTINE.description

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private lateinit var textRemainingTime: TextView
    private lateinit var buttonStart: Button

    private var job: Job? = null

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

            job = coroutineScope.launch {
                buttonStart.isEnabled = false

                val iterationsCount = executeBenchmark()
                showIterationsCountMessage(iterationsCount)

                buttonStart.isEnabled = true
            }
        }

        return view
    }

    override fun onStop() {
        logThreadInfo("onStop()")
        super.onStop()

        job?.cancel()
        buttonStart.isEnabled = true
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

    private fun updateRemainingTime(remainingSeconds: Int) {
        logThreadInfo("updateRemainingTime: $remainingSeconds seconds")

        if (remainingSeconds > 0) {
            textRemainingTime.text = String.format(
                Locale.getDefault(),
                getString(R.string.template_remaining_time),
                remainingSeconds
            )

            Handler(Looper.getMainLooper()).postDelayed({
                updateRemainingTime(remainingSeconds - 1)
            }, 1000)
        } else {
            textRemainingTime.text = getString(R.string.message_done)
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