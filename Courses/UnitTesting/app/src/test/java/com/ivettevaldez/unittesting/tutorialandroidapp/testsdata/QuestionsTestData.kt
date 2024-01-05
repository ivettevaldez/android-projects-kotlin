package com.ivettevaldez.unittesting.tutorialandroidapp.testsdata

import com.ivettevaldez.unittesting.tutorialandroidapp.questions.Question
import com.ivettevaldez.unittesting.tutorialandroidapp.questions.QuestionDetails

object QuestionsTestData {

    private const val ID_1 = "id1"
    private const val ID_2 = "id2"
    private const val TITLE_1 = "title1"
    private const val TITLE_2 = "title2"
    private const val BODY_1 = "body1"
    private const val BODY_2 = "body2"

    fun getQuestion(): Question = Question(ID_1, TITLE_1)

    fun getQuestions(): List<Question> {
        return listOf(
            Question(ID_1, TITLE_1),
            Question(ID_2, TITLE_2)
        )
    }

    fun getQuestionDetails1(): QuestionDetails {
        return QuestionDetails(ID_1, TITLE_1, BODY_1)
    }

    fun getQuestionDetails2(): QuestionDetails {
        return QuestionDetails(ID_2, TITLE_2, BODY_2)
    }
}