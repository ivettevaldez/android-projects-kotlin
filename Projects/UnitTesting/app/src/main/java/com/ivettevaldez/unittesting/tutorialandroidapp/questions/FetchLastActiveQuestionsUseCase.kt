package com.ivettevaldez.unittesting.tutorialandroidapp.questions

import com.ivettevaldez.unittesting.tutorialandroidapp.common.BaseObservable
import com.ivettevaldez.unittesting.tutorialandroidapp.networking.questions.list.FetchLastActiveQuestionsEndpoint

class FetchLastActiveQuestionsUseCase(
    private val fetchLastActiveQuestionsEndpoint: FetchLastActiveQuestionsEndpoint
) : BaseObservable<FetchLastActiveQuestionsUseCase.Listener>() {

    interface Listener {

    }
}