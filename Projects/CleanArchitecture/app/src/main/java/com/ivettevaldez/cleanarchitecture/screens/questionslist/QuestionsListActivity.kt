package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.ivettevaldez.cleanarchitecture.R
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.networking.questions.QuestionSchema
import com.ivettevaldez.cleanarchitecture.networking.questions.QuestionsListResponseSchema
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.Constants
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseActivity
import kotlinx.android.synthetic.main.activity_questions_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuestionsListActivity : BaseActivity(), QuestionsListAdapter.Listener {

    private lateinit var listQuestions: ListView
    private lateinit var questionsListAdapter: QuestionsListAdapter
    private lateinit var stackOverflowApi: StackOverflowApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_list)

        questionsListAdapter = QuestionsListAdapter(this, this)
        listQuestions = questions_list_items
        listQuestions.adapter = questionsListAdapter

        stackOverflowApi = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StackOverflowApi::class.java)
    }

    override fun onStart() {
        super.onStart()
        fetchQuestions()
    }

    private fun fetchQuestions() {
        stackOverflowApi.fetchLastActiveQuestions(Constants.QUESTIONS_LIST_PAGE_SIZE)!!
            .enqueue(
                object : Callback<QuestionsListResponseSchema?> {
                    override fun onResponse(
                        call: Call<QuestionsListResponseSchema?>,
                        response: Response<QuestionsListResponseSchema?>
                    ) {
                        if (response.isSuccessful) {
                            bindQuestions(response.body()?.questions)
                        } else {
                            networkCallFailed()
                        }
                    }

                    override fun onFailure(
                        call: Call<QuestionsListResponseSchema?>,
                        throwable: Throwable
                    ) {
                        networkCallFailed()
                    }
                }
            )
    }

    private fun bindQuestions(questionsSchemas: List<QuestionSchema>?) {
        if (questionsSchemas != null) {
            val questions = mutableListOf<Question>()

            for (questionSchema in questionsSchemas) {
                questions.add(
                    Question(
                        questionSchema.id,
                        questionSchema.title
                    )
                )
            }

            questionsListAdapter.clear()
            questionsListAdapter.addAll(questions)
            questionsListAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(
                applicationContext,
                "Oops! An error has occurred",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun networkCallFailed() {
        Toast.makeText(applicationContext, "Network call failed", Toast.LENGTH_SHORT).show()
    }

    override fun onQuestionClicked(question: Question?) {
        Toast.makeText(applicationContext, "${question?.title}", Toast.LENGTH_SHORT).show()
    }
}