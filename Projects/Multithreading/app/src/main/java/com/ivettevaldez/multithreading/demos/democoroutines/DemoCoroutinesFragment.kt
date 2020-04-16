package com.ivettevaldez.multithreading.demos.democoroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ivettevaldez.multithreading.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DemoCoroutinesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DemoCoroutinesFragment : Fragment() {

    private lateinit var textTime: TextView
    private lateinit var textMessages: TextView
    private lateinit var progress: ProgressBar
    private lateinit var buttonStart: Button

    private lateinit var producerConsumerBenchmarkUseCase: ProducerConsumerBenchmarkUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        producerConsumerBenchmarkUseCase = ProducerConsumerBenchmarkUseCase()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_demo_coroutines, container, false)

        initViews(view)

        return view
    }

    private fun onBenchmarkCompleted(result: ProducerConsumerBenchmarkUseCase.Result?) {
        progress.visibility = View.INVISIBLE
        buttonStart.isEnabled = true
        textTime.text = String.format(
                Locale.getDefault(),
                "Execution time: %d ms",
                result?.executionTime
        )
        textMessages.text = String.format(
                Locale.getDefault(),
                "Received messages: %d",
                result?.numberOfMessages
        )
    }

    private fun startCoroutine() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = producerConsumerBenchmarkUseCase.startBenchmark()
            onBenchmarkCompleted(result)
        }
    }

    private fun initViews(view: View) {
        textTime = view.findViewById(R.id.demo_coroutines_text_time)
        textMessages = view.findViewById(R.id.demo_coroutines_text_messages)
        progress = view.findViewById(R.id.demo_coroutines_progress)
        buttonStart = view.findViewById(R.id.demo_coroutines_button_start)

        buttonStart.setOnClickListener {
            buttonStart.isEnabled = false
            textTime.text = ""
            textMessages.text = ""
            progress.visibility = View.VISIBLE

            startCoroutine()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = DemoCoroutinesFragment()
    }
}
