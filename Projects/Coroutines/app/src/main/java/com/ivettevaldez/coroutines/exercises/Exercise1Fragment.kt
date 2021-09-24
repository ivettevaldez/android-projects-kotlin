package com.ivettevaldez.coroutines.exercises

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ivettevaldez.coroutines.R
import com.ivettevaldez.coroutines.common.BaseFragment
import com.ivettevaldez.coroutines.common.ThreadInfoLogger
import com.ivettevaldez.coroutines.home.ScreenReachableFromHome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Exercise1Fragment : BaseFragment() {

    override val screenTitle get() = ScreenReachableFromHome.EXERCISE_1.description

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private lateinit var editUserId: EditText
    private lateinit var buttonGetReputation: Button

    private lateinit var getReputationEndpoint: GetReputationEndpoint

    companion object {

        @JvmStatic
        fun newInstance() = Exercise1Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getReputationEndpoint = compositionRoot.getReputationEndpoint
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exercise1, container, false)

        editUserId = view.findViewById(R.id.exercise1_edit_user_id)
        editUserId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonGetReputation.isEnabled = !charSequence.isNullOrBlank()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        buttonGetReputation = view.findViewById(R.id.exercise1_button_get_reputation)
        buttonGetReputation.setOnClickListener {
            logThreadInfo("Button callback")

            coroutineScope.launch {
                buttonGetReputation.isEnabled = false

                val id = editUserId.text.toString()
                val reputation = getReputationForUser(id)

                Toast.makeText(
                    requireContext(),
                    "Reputation: $reputation",
                    Toast.LENGTH_SHORT
                ).show()

                buttonGetReputation.isEnabled = true
            }
        }

        return view
    }

    private suspend fun getReputationForUser(userId: String): Int {
        return withContext(Dispatchers.Default) {
            logThreadInfo("getReputation()")
            getReputationEndpoint.getReputation(userId)
        }
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }
}