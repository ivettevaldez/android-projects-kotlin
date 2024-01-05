package com.ivettevaldez.unittesting.tutorialandroidapp.questions

import com.ivettevaldez.unittesting.tutorialandroidapp.common.BaseObservable
import com.ivettevaldez.unittesting.tutorialandroidapp.common.time.TimeProvider
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details.FetchQuestionDetailsEndpoint
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details.QuestionDetailsSchema

open class FetchQuestionDetailsUseCase(
    private val fetchQuestionDetailsEndpoint: FetchQuestionDetailsEndpoint?,
    private val timeProvider: TimeProvider?
) : BaseObservable<FetchQuestionDetailsUseCase.Listener>(),
    FetchQuestionDetailsEndpoint.Listener {

    interface Listener {

        fun onQuestionDetailsFetched(details: QuestionDetails)
        fun onFetchQuestionDetailsFailed()
    }

    companion object {

        private const val CACHE_TIMEOUT_MS = 60000L
    }

    private val cachedQuestionDetails: HashMap<String, QuestionDetailsCacheEntry> = HashMap()

    open fun fetchAndNotify(questionId: String) {
        if (serveQuestionDetailsFromCacheIfValid(questionId)) {
            return
        }

        fetchQuestionDetailsEndpoint!!.fetchQuestionDetails(questionId, this)
    }

    override fun onQuestionDetailsFetched(questionDetailsSchema: QuestionDetailsSchema?) {
        if (questionDetailsSchema != null) {
            val questionDetails = getQuestionDetailsFromSchema(questionDetailsSchema)

            saveEntryInCache(questionDetails)
            notifySuccess(questionDetails)
        } else {
            notifyFailure()
        }
    }

    override fun onQuestionDetailsFetchFailed() {
        notifyFailure()
    }

    private fun serveQuestionDetailsFromCacheIfValid(questionId: String): Boolean {
        val questionDetailsCacheEntry = cachedQuestionDetails[questionId]

        return if (questionDetailsCacheEntry != null &&
            timeProvider!!.getCurrentTimestamp() < CACHE_TIMEOUT_MS - questionDetailsCacheEntry.timestamp
        ) {
            notifySuccess(questionDetailsCacheEntry.questionDetails)
            true
        } else {
            false
        }
    }

    private fun saveEntryInCache(questionDetails: QuestionDetails) {
        cachedQuestionDetails[questionDetails.id] = QuestionDetailsCacheEntry(
            questionDetails,
            timeProvider!!.getCurrentTimestamp()
        )
    }

    private fun getQuestionDetailsFromSchema(schema: QuestionDetailsSchema): QuestionDetails {
        return QuestionDetails(
            id = schema.id,
            title = schema.title,
            body = schema.body
        )
    }

    private fun notifySuccess(questionDetails: QuestionDetails) {
        for (listener in listeners) {
            listener.onQuestionDetailsFetched(questionDetails)
        }
    }

    private fun notifyFailure() {
        for (listener in listeners) {
            listener.onFetchQuestionDetailsFailed()
        }
    }

    private data class QuestionDetailsCacheEntry(
        val questionDetails: QuestionDetails,
        val timestamp: Long
    )
}