package com.ivettevaldez.dependencyinjection.screens.questiondetails

/* ktlint-disable no-wildcard-imports */

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ivettevaldez.dependencyinjection.R
import com.ivettevaldez.dependencyinjection.common.Constants
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.ServerErrorDialogFragment
import com.ivettevaldez.dependencyinjection.screens.common.toolbar.MyToolbar
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val EXTRA_QUESTION_ID = "EXTRA_QUESTION_ID"

class QuestionDetailsActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var questionId: String = ""

    private lateinit var toolbar: MyToolbar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var textBody: TextView

    private lateinit var stackOverflowApi: StackOverflowApi

    companion object {

        fun start(context: Context, questionId: String) {
            val intent = Intent(context, QuestionDetailsActivity::class.java)
            intent.putExtra(EXTRA_QUESTION_ID, questionId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_details)

        textBody = findViewById(R.id.question_details_text_body)

        // Init toolbar
        toolbar = findViewById(R.id.question_details_toolbar)
        toolbar.setNavigateUpListener {
            onBackPressed()
        }

        // Init pull-down-to-refresh
        swipeRefresh = findViewById(R.id.question_details_swipe_refresh)
        swipeRefresh.isEnabled = false

        // Init Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        stackOverflowApi = retrofit.create(StackOverflowApi::class.java)

        questionId = intent.extras!!.getString(EXTRA_QUESTION_ID)!!
    }

    override fun onStart() {
        super.onStart()
        fetchQuestionDetails()
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
    }

    private fun fetchQuestionDetails() {
        coroutineScope.launch {
            showProgressIndicator()

            try {
                val response = stackOverflowApi.questionDetails(questionId)
                if (response.isSuccessful && response.body() != null) {
                    val questionBody = response.body()!!.question.body
                    textBody.text = Html.fromHtml(questionBody, Html.FROM_HTML_MODE_LEGACY)
                }
            } catch (t: Throwable) {
                if (t is CancellationException) {
                    onFetchFailed()
                }
            } finally {
                hideProgressIndicator()
            }
        }
    }

    private fun onFetchFailed() {
        supportFragmentManager.beginTransaction()
            .add(ServerErrorDialogFragment.newInstance(this), null)
            .commitAllowingStateLoss()
    }

    private fun showProgressIndicator() {
        swipeRefresh.isRefreshing = true
    }

    private fun hideProgressIndicator() {
        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false
        }
    }
}