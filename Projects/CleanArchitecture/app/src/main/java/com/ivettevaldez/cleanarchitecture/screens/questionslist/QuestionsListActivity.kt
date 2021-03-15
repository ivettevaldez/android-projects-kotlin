package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.os.Bundle
import android.widget.Toast
import com.ivettevaldez.cleanarchitecture.common.Constants
import com.ivettevaldez.cleanarchitecture.networking.StackOverflowApi
import com.ivettevaldez.cleanarchitecture.networking.questions.QuestionSchema
import com.ivettevaldez.cleanarchitecture.networking.questions.QuestionsListResponseSchema
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionsListActivity : BaseActivity(), IQuestionsListViewMvc.Listener {

    private lateinit var stackOverflowApi: StackOverflowApi
    private lateinit var viewMvc: IQuestionsListViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stackOverflowApi = getCompositionRoot().getStackOverflowApi()

        viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getQuestionsListViewMvc(null)

        setContentView(viewMvc.getRootView())
    }

    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)
        fetchQuestions()
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
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

            viewMvc.bindQuestions(questions)
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