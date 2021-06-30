package com.ivettevaldez.unittesting.tutorialandroidapp.testsdata

import com.ivettevaldez.unittesting.tutorialandroidapp.questions.Question

object QuestionsTestData {

    const val ID_1 = "id1"
    const val ID_2 = "id2"
    const val TITLE_1 = "title1"
    const val TITLE_2 = "title2"
    const val BODY_1 = "body1"
    const val BODY_2 = "body2"

    fun getQuestion(): Question = Question(ID_1, TITLE_1)

    fun getQuestions(): List<Question> {
        return listOf(
            Question(ID_1, TITLE_1),
            Question(ID_2, TITLE_2)
        )
    }
}