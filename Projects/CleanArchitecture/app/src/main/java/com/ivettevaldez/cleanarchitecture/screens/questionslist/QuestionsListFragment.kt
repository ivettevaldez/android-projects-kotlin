package com.ivettevaldez.cleanarchitecture.screens.questionslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionsUseCase
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseFragment
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsManager
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.promptdialog.PromptDialogEvent
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreensNavigator

private const val SAVED_STATE_SCREEN_STATE = "SAVED_STATE_SCREEN_STATE"

class QuestionsListFragment : BaseFragment(),
    IQuestionsListViewMvc.Listener,
    FetchQuestionsUseCase.Listener,
    DialogsEventBus.Listener {

    private var screenState: ScreenState = ScreenState.IDLE

    private lateinit var fetchQuestionsUseCase: FetchQuestionsUseCase
    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var dialogsManager: DialogsManager
    private lateinit var dialogsEventBus: DialogsEventBus
    private lateinit var viewMvc: IQuestionsListViewMvc

    enum class ScreenState {
        IDLE, FETCHING_QUESTIONS, QUESTIONS_SHOWN, NETWORK_ERROR
    }

    companion object {

        fun newInstance(): QuestionsListFragment = QuestionsListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            screenState = savedInstanceState.getSerializable(
                SAVED_STATE_SCREEN_STATE
            ) as ScreenState
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fetchQuestionsUseCase = getCompositionRoot().getFetchQuestionsUseCase()
        screensNavigator = getCompositionRoot().getScreensNavigator()
        dialogsManager = getCompositionRoot().getDialogsManager()
        dialogsEventBus = getCompositionRoot().getDialogsEventBus()

        viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getQuestionsListViewMvc(container)

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        fetchQuestionsUseCase.registerListener(this)
        dialogsEventBus.registerListener(this)

        viewMvc.registerListener(this)

        if (screenState != ScreenState.NETWORK_ERROR) {
            fetchQuestionsAndNotify()
        }
    }

    override fun onStop() {
        super.onStop()

        fetchQuestionsUseCase.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
        viewMvc.unregisterListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVED_STATE_SCREEN_STATE, screenState)
    }

    override fun onQuestionClicked(question: Question?) {
        if (question != null) {
            screensNavigator.toQuestionDetails(question.id)
        } else {
            dialogsManager.showNullQuestionError(null)
        }
    }

    override fun onDialogEvent(event: Any) {
        if (event is PromptDialogEvent) {
            when (event.clickedButton) {
                PromptDialogEvent.Button.POSITIVE -> {
                    fetchQuestionsAndNotify()
                }
                PromptDialogEvent.Button.NEGATIVE -> {
                    screenState = ScreenState.IDLE
                }
            }
        }
    }

    override fun onQuestionsFetched(questions: List<Question>) {
        screenState = ScreenState.QUESTIONS_SHOWN
        viewMvc.showProgressIndicator(false)
        viewMvc.bindQuestions(questions)
    }

    override fun onQuestionsFetchFailed() {
        screenState = ScreenState.NETWORK_ERROR
        viewMvc.showProgressIndicator(false)
        dialogsManager.showUseCaseError(null)
    }

    private fun fetchQuestionsAndNotify() {
        screenState = ScreenState.FETCHING_QUESTIONS
        viewMvc.showProgressIndicator(true)
        fetchQuestionsUseCase.executeAndNotify()
    }
}