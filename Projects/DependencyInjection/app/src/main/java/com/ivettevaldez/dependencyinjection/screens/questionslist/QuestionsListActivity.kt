package com.ivettevaldez.dependencyinjection.screens.questionslist

/* ktlint-disable no-wildcard-imports */

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.dependencyinjection.common.Constants
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.ServerErrorDialogFragment
import com.ivettevaldez.dependencyinjection.screens.questiondetails.QuestionDetailsActivity
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuestionsListActivity : AppCompatActivity(),
    IQuestionsListViewMvc.Listener {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var isDataLoaded: Boolean = false

    private lateinit var stackOverflowApi: StackOverflowApi
    private lateinit var viewMvc: QuestionsListViewMvcImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewMvc = QuestionsListViewMvcImpl(LayoutInflater.from(this), null)

        setContentView(viewMvc.rootView)

        // Init Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        stackOverflowApi = retrofit.create(StackOverflowApi::class.java)
    }

    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)

        if (!isDataLoaded) {
            fetchQuestions()
        }
    }

    override fun onStop() {
        super.onStop()

        viewMvc.unregisterListener(this)
        coroutineScope.coroutineContext.cancelChildren()
    }

    override fun onQuestionClicked(questionId: String) {
        QuestionDetailsActivity.start(this, questionId)
    }

    override fun onRefreshClicked() {
        fetchQuestions()
    }

    private fun fetchQuestions() {
        coroutineScope.launch {
            viewMvc.showProgressIndicator()

            try {
                val response = stackOverflowApi.lastActiveQuestions(20)
                if (response.isSuccessful && response.body() != null) {
                    viewMvc.bindQuestions(response.body()!!.questions)
                    isDataLoaded = true
                }
            } catch (t: Throwable) {
                if (t is CancellationException) {
                    onFetchFailed()
                }
            } finally {
                viewMvc.hideProgressIndicator()
            }
        }
    }

    private fun onFetchFailed() {
        supportFragmentManager.beginTransaction()
            .add(ServerErrorDialogFragment.newInstance(this), null)
            .commitAllowingStateLoss()
    }
}