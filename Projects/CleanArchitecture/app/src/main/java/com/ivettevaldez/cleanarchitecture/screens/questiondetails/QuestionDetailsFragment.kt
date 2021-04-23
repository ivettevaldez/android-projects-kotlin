package com.ivettevaldez.cleanarchitecture.screens.questiondetails

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.cleanarchitecture.common.permissions.PermissionsHelper
import com.ivettevaldez.cleanarchitecture.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.cleanarchitecture.questions.Question
import com.ivettevaldez.cleanarchitecture.screens.common.controllers.BaseFragment
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.DialogsManager
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.promptdialog.PromptDialogEvent
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.promptdialog.PromptDialogEvent.Button.NEGATIVE
import com.ivettevaldez.cleanarchitecture.screens.common.dialogs.promptdialog.PromptDialogEvent.Button.POSITIVE
import com.ivettevaldez.cleanarchitecture.screens.common.navigation.ScreensNavigator

private const val ARG_QUESTION_ID = "ARG_QUESTION_ID"
private const val SAVED_STATE_SCREEN_STATE = "SAVED_STATE_SCREEN_STATE"
private const val REQUEST_CODE_LOCATION = 1001

class QuestionDetailsFragment : BaseFragment(),
    IQuestionDetailsViewMvc.Listener,
    FetchQuestionDetailsUseCase.Listener,
    DialogsEventBus.Listener,
    PermissionsHelper.Listener {

    private var screenState: ScreenState = ScreenState.IDLE

    private lateinit var questionId: String

    private lateinit var fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase
    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var dialogsManager: DialogsManager
    private lateinit var dialogsEventBus: DialogsEventBus
    private lateinit var permissionsHelper: PermissionsHelper
    private lateinit var viewMvc: IQuestionDetailsViewMvc

    enum class ScreenState {
        IDLE, FETCHING_QUESTION_DETAILS, QUESTION_DETAILS_SHOWN, NETWORK_ERROR
    }

    companion object {

        @JvmStatic
        fun newInstance(questionId: String): QuestionDetailsFragment {
            return QuestionDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUESTION_ID, questionId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionId = it.getString(ARG_QUESTION_ID)!!
        }

        if (savedInstanceState != null) {
            screenState = savedInstanceState.getSerializable(
                SAVED_STATE_SCREEN_STATE
            ) as ScreenState
        }

        fetchQuestionDetailsUseCase = getCompositionRoot().getFetchQuestionDetailsUseCase()
        screensNavigator = getCompositionRoot().getScreensNavigator()
        dialogsManager = getCompositionRoot().getDialogsManager()
        dialogsEventBus = getCompositionRoot().getDialogsEventBus()
        permissionsHelper = getCompositionRoot().getPermissionsHelper()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewMvc = getCompositionRoot()
            .getViewMvcFactory()
            .getQuestionDetailsViewMvc(container)

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        fetchQuestionDetailsUseCase.registerListener(this)
        dialogsEventBus.registerListener(this)
        permissionsHelper.registerListener(this)

        viewMvc.registerListener(this)

        if (screenState != ScreenState.NETWORK_ERROR) {
            fetchQuestionDetailsAndNotify(questionId)
        }
    }

    override fun onStop() {
        super.onStop()

        fetchQuestionDetailsUseCase.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
        permissionsHelper.unregisterListener(this)

        viewMvc.unregisterListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVED_STATE_SCREEN_STATE, screenState)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onLocationRequestClicked() {
        if (permissionsHelper.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            dialogsManager.showPermissionGranted(null)
        } else {
            permissionsHelper.requestPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onDialogEvent(event: Any) {
        if (event is PromptDialogEvent) {
            when (event.clickedButton) {
                POSITIVE -> {
                    fetchQuestionDetailsAndNotify(questionId)
                }
                NEGATIVE -> {
                    screenState = ScreenState.IDLE
                }
            }
        }
    }

    override fun onPermissionGranted(permission: String, requestCode: Int) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            dialogsManager.showPermissionGranted(null)
        }
    }

    override fun onPermissionDeclined(permission: String, requestCode: Int) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            dialogsManager.showPermissionDeclined(null)
        }
    }

    override fun onPermissionDeclinedAndDontAskAgain(permission: String, requestCode: Int) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            dialogsManager.showPermissionDeclinedAndDontAskAgain(null)
        }
    }

    override fun onQuestionDetailsFetched(question: Question) {
        screenState = ScreenState.QUESTION_DETAILS_SHOWN
        viewMvc.showProgressIndicator(false)
        viewMvc.bindQuestion(question)
    }

    override fun onQuestionDetailsFetchFailed() {
        screenState = ScreenState.NETWORK_ERROR
        viewMvc.showProgressIndicator(false)
        dialogsManager.showUseCaseError(null)
    }

    private fun fetchQuestionDetailsAndNotify(questionId: String) {
        screenState = ScreenState.FETCHING_QUESTION_DETAILS
        viewMvc.showProgressIndicator(true)
        fetchQuestionDetailsUseCase.executeAndNotify(questionId)
    }
}