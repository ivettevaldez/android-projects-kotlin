package com.ivettevaldez.coroutines.exercises.exercise2

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
import com.ivettevaldez.coroutines.common.ThreadInfoLogger
import com.ivettevaldez.coroutines.exercises.GetReputationEndpoint
import com.ivettevaldez.coroutines.home.ScreenReachableFromHome
import kotlinx.coroutines.*
import java.util.*

class Exercise2Fragment : BaseFragment() {

    override val screenTitle get() = ScreenReachableFromHome.EXERCISE_2.description

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private lateinit var editUserId: EditText
    private lateinit var buttonGetReputation: Button
    private lateinit var textElapsedTime: TextView
    private lateinit var getReputationEndpoint: GetReputationEndpoint

    private var jobElapsedTime: Job? = null

    companion object {

        private const val MILLISECOND: Long = 1_000_000L
        private const val DELAY: Long = 100L

        @JvmStatic
        fun newInstance() = Exercise2Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getReputationEndpoint = compositionRoot.getReputationEndpoint
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_exercise2, container, false)

        editUserId = view.findViewById(R.id.exercise2_edit_user_id)
        editUserId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonGetReputation.isEnabled = !charSequence.isNullOrBlank()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        buttonGetReputation = view.findViewById(R.id.exercise2_button_get_reputation)
        buttonGetReputation.setOnClickListener {
            logThreadInfo("Button callback")

            jobElapsedTime = coroutineScope.launch {
                updateElapsedTime()
            }

            coroutineScope.launch {
                buttonGetReputation.isEnabled = false

                val id = editUserId.text.toString()
                val reputation = getReputationForUser(id)

                showReputationMessage(reputation)

                buttonGetReputation.isEnabled = true
                jobElapsedTime?.cancel()
            }
        }

        textElapsedTime = view.findViewById(R.id.exercise2_text_elapsed_time)

        return view
    }

    override fun onStop() {
        logThreadInfo("onStart()")
        super.onStop()

        coroutineScope.coroutineContext.cancelChildren()
        buttonGetReputation.isEnabled = true
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

    private fun showReputationMessage(reputation: Int) {
        val message = String.format(
            Locale.getDefault(),
            getString(R.string.template_reputation),
            reputation
        )
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private suspend fun getReputationForUser(userId: String): Int =
        withContext(Dispatchers.Default) {
            logThreadInfo("getReputation()")
            getReputationEndpoint.getReputation(userId)
        }

    private suspend fun updateElapsedTime() {
        textElapsedTime.visibility = View.VISIBLE
        val startTimeNano = System.nanoTime()

        while (true) {
            val elapsedTime = (System.nanoTime() - startTimeNano) / MILLISECOND

            textElapsedTime.text = String.format(
                Locale.getDefault(),
                getString(R.string.template_elapsed_time_ms),
                elapsedTime
            )
            delay(DELAY)
        }
    }
}