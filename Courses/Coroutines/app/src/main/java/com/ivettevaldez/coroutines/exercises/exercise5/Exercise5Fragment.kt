package com.ivettevaldez.coroutines.exercises.exercise5

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ivettevaldez.coroutines.R
import com.ivettevaldez.coroutines.common.BaseFragment
import com.ivettevaldez.coroutines.home.ScreenReachableFromHome
import kotlinx.coroutines.*
import com.ivettevaldez.coroutines.common.ThreadInfoLogger as logger

class Exercise5Fragment : BaseFragment() {

    override val screenTitle: String
        get() = ScreenReachableFromHome.EXERCISE_3.description

    private lateinit var getReputationUseCase: GetReputationUseCase

    private lateinit var editUserId: EditText
    private lateinit var buttonGetReputation: Button
    private lateinit var textElapsedTime: TextView

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private var jobElapsedTime: Job? = null

    companion object {

        private const val ELAPSED_TIME_UPDATE_MILLIS: Long = 100L
        private const val MS_VALUE: Long = 1_000_000L

        fun newInstance() = Exercise5Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getReputationUseCase = compositionRoot.getReputationUseCase
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_5, container, false)

        editUserId = view.findViewById(R.id.edt_user_id)
        editUserId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonGetReputation.isEnabled = !editUserId.text.isNullOrEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        textElapsedTime = view.findViewById(R.id.txt_elapsed_time)

        buttonGetReputation = view.findViewById(R.id.btn_get_reputation)
        buttonGetReputation.setOnClickListener {
            logger.logThreadInfo("button callback")

            jobElapsedTime = coroutineScope.launch {
                updateElapsedTime()
                this.cancel()
            }

            coroutineScope.launch {
                // This must be executed on the Main thread
                buttonGetReputation.isEnabled = false

                // This must be executed on a Background thread
                val reputation =
                    getReputationUseCase.getReputationForUser(editUserId.text.toString())

                // This must be executed on the Main thread
                Toast.makeText(
                    requireContext(), "reputation: $reputation", Toast.LENGTH_SHORT
                ).show()
                buttonGetReputation.isEnabled = true

                jobElapsedTime?.cancel()
            }
        }

        return view
    }

    override fun onStop() {
        logger.logThreadInfo("onStop()")
        super.onStop()
        // Cancel coroutine
        coroutineScope.coroutineContext.cancelChildren()
        // Reset UI state
        buttonGetReputation.isEnabled = true
    }

    private suspend fun updateElapsedTime() {
        val startTimeNano = System.nanoTime()

        while (true) {
            delay(ELAPSED_TIME_UPDATE_MILLIS)
            val elapsedTimeMillis = (System.nanoTime() - startTimeNano) / MS_VALUE
            textElapsedTime.text = getString(
                R.string.elapsed_time_ms_template,
                elapsedTimeMillis.toString()
            )
        }
    }
}