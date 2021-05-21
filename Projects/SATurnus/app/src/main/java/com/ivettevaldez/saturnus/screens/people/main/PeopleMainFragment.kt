package com.ivettevaldez.saturnus.screens.people.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.saturnus.people.ClientType
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet.PromptBottomSheetDialogEvent
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PeopleMainFragment : BaseFragment(),
    IPeopleMainViewMvc.Listener,
    DialogsEventBus.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var dialogsManager: DialogsManager

    @Inject
    lateinit var dialogsEventBus: DialogsEventBus

    private lateinit var viewMvc: IPeopleMainViewMvc

    companion object {

        private const val TAG_PEOPLE_MAIN_OPTIONS_DIALOG = "TAG_PEOPLE_MAIN_OPTIONS_DIALOG"

        @JvmStatic
        fun newInstance() = PeopleMainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newPeopleMainViewMvc(parent)

        viewMvc.setViewPager(
            // TODO: Fix this.
            PeopleMainPagerAdapter(requireActivity() as AppCompatActivity),
            PeopleMainPagerAdapter.TAB_CLIENT_TYPE_ISSUING
        )

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)
        dialogsEventBus.registerListener(this)
    }

    override fun onStop() {
        super.onStop()

        viewMvc.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        screensNavigator.navigateUp()
    }

    override fun onAddNewClicked() {
        dialogsManager.showSelectClientTypeDialog(TAG_PEOPLE_MAIN_OPTIONS_DIALOG)
    }

    override fun onDialogEvent(event: Any) {
        if (dialogsManager.getShownDialogTag() == TAG_PEOPLE_MAIN_OPTIONS_DIALOG) {
            validateSelectedOption(
                (event as PromptBottomSheetDialogEvent).clickedButton
            )
        }
    }

    private fun validateSelectedOption(clickedButton: PromptBottomSheetDialogEvent.Button) {
        Thread {
            val clientType = when (clickedButton) {
                PromptBottomSheetDialogEvent.Button.OPTION_ONE -> ClientType.Type.ISSUING
                PromptBottomSheetDialogEvent.Button.OPTION_TWO -> ClientType.Type.RECEIVER
            }
            screensNavigator.toPersonForm(null, clientType)
        }.start()
    }
}