package com.ivettevaldez.coroutines.exercises.exercise1

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
import com.ivettevaldez.coroutines.home.ScreenReachableFromHome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.ivettevaldez.coroutines.common.ThreadInfoLogger as logger

class Exercise1Fragment : BaseFragment() {

    override val screenTitle: String
        get() = ScreenReachableFromHome.EXERCISE_1.description

    private lateinit var editUserId: EditText
    private lateinit var buttonGetReputation: Button

    private lateinit var getReputationEndpoint: GetReputationEndpoint

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    companion object {

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
        val view = inflater.inflate(R.layout.fragment_exercise1, container, false)

        editUserId = view.findViewById(R.id.edt_user_id)
        editUserId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonGetReputation.isEnabled = !editUserId.text.isNullOrEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        buttonGetReputation = view.findViewById(R.id.btn_get_reputation)
        buttonGetReputation.setOnClickListener {
            logger.logThreadInfo("button callback")

            coroutineScope.launch {
                // This must be executed on the Main thread
                it.isEnabled = false

                // This must be executed on a Background thread
                val reputation = getReputationForUser(editUserId.text.toString())

                // This must be executed on the Main thread
                Toast.makeText(
                    requireContext(), "reputation: $reputation", Toast.LENGTH_SHORT
                ).show()
                it.isEnabled = true
            }
        }

        return view
    }

    private suspend fun getReputationForUser(userId: String): Int {
        return withContext(Dispatchers.Default) {
            logger.logThreadInfo("")
            getReputationEndpoint.getReputation(userId)
        }
    }
}