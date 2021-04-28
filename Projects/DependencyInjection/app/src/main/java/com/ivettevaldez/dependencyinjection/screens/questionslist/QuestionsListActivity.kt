package com.ivettevaldez.dependencyinjection.screens.questionslist

/* ktlint-disable no-wildcard-imports */

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ivettevaldez.dependencyinjection.R
import com.ivettevaldez.dependencyinjection.common.Constants
import com.ivettevaldez.dependencyinjection.networking.StackOverflowApi
import com.ivettevaldez.dependencyinjection.questions.Question
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.ServerErrorDialogFragment
import com.ivettevaldez.dependencyinjection.screens.questiondetails.QuestionDetailsActivity
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuestionsListActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var isDataLoaded: Boolean = false

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var questionsAdapter: QuestionsAdapter
    private lateinit var stackOverflowApi: StackOverflowApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_list)

        // Init pull-down-to-refresh
        swipeRefresh = findViewById(R.id.questions_list_swipe_refresh)
        swipeRefresh.setOnRefreshListener {
            fetchQuestions()
        }

        // Init RecyclerView
        questionsAdapter = QuestionsAdapter { clickedQuestion ->
            QuestionDetailsActivity.start(this, clickedQuestion.id)
        }
        recyclerView = findViewById(R.id.questions_list_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = questionsAdapter
        recyclerView.addItemDecoration(
            getDividerItemDecoration(this)
        )

        // Init Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        stackOverflowApi = retrofit.create(StackOverflowApi::class.java)
    }

    override fun onStart() {
        super.onStart()
        if (!isDataLoaded) {
            fetchQuestions()
        }
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
    }

    private fun fetchQuestions() {
        coroutineScope.launch {
            showProgressIndicator()

            try {
                val response = stackOverflowApi.lastActiveQuestions(20)
                if (response.isSuccessful && response.body() != null) {
                    questionsAdapter.bindData(response.body()!!.questions)
                    isDataLoaded = true
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

    private fun getDividerItemDecoration(context: Context): DividerItemDecoration {
        val divider = ContextCompat.getDrawable(context, R.drawable.shape_divider)
        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )

        if (divider != null) {
            dividerItemDecoration.setDrawable(divider)
        }

        return dividerItemDecoration
    }
}

class QuestionsAdapter(
    private val onQuestionClickedListener: (Question) -> Unit
) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    private var questionsList: List<Question> = ArrayList(0)

    inner class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitle: TextView = view.findViewById(R.id.item_question_text_title)
    }

    fun bindData(questions: List<Question>) {
        questionsList = ArrayList(questions)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.textTitle.text = questionsList[position].title
        holder.itemView.setOnClickListener {
            onQuestionClickedListener.invoke(questionsList[position])
        }
    }

    override fun getItemCount(): Int {
        return questionsList.size
    }
}