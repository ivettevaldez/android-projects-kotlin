package com.ivettevaldez.unittesting.tutorialandroidapp.common.dependencyinjection

import com.ivettevaldez.unittesting.tutorialandroidapp.common.Constants
import com.ivettevaldez.unittesting.tutorialandroidapp.common.time.TimeProvider
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.StackOverflowApi
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.details.FetchQuestionDetailsEndpoint
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.FetchQuestionDetailsUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CompositionRoot {

    private var retrofit: Retrofit? = null
    private var fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase? = null

    fun getTimeProvider(): TimeProvider = TimeProvider()

    fun getStackOverflowApi(): StackOverflowApi {
        return getRetrofit().create(StackOverflowApi::class.java)
    }

    private fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    private fun getFetchQuestionDetailsEndpoint() =
        FetchQuestionDetailsEndpoint(getStackOverflowApi())

    fun getFetchQuestionDetailsUseCase(): FetchQuestionDetailsUseCase {
        if (fetchQuestionDetailsUseCase == null) {
            fetchQuestionDetailsUseCase = FetchQuestionDetailsUseCase(
                getFetchQuestionDetailsEndpoint(),
                getTimeProvider()
            )
        }
        return fetchQuestionDetailsUseCase!!
    }
}