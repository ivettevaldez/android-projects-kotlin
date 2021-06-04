package com.ivettevaldez.saturnus.screens.invoices.form.main

/* ktlint-disable no-wildcard-imports */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.FragmentsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsEventBus
import com.ivettevaldez.saturnus.screens.common.dialogs.DialogsManager
import com.ivettevaldez.saturnus.screens.common.dialogs.prompt.PromptDialogEvent
import com.ivettevaldez.saturnus.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import com.ivettevaldez.saturnus.screens.invoices.form.InvoiceChangeFragmentEvent
import javax.inject.Inject

class InvoiceFormMainFragment : BaseFragment(),
    IInvoiceFormMainViewMvc.Listener,
    DialogsEventBus.Listener, FragmentsEventBus.Listener {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var screensNavigator: ScreensNavigator

    @Inject
    lateinit var fragmentsEventBus: FragmentsEventBus

    @Inject
    lateinit var dialogsEventBus: DialogsEventBus

    @Inject
    lateinit var dialogsManager: DialogsManager

    private lateinit var viewMvc: IInvoiceFormMainViewMvc
    private lateinit var issuingRfc: String

    private var hasFormChanges: Boolean = false

    companion object {

        private const val ARG_ISSUING_RFC = "ARG_ISSUING_RFC"

        @JvmStatic
        fun newInstance(issuingRfc: String) =
            InvoiceFormMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ISSUING_RFC, issuingRfc)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)

        requireArguments().let {
            issuingRfc = it.getString(ARG_ISSUING_RFC)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = viewMvcFactory.newInvoiceFormMainViewMvc(parent)
        viewMvc.initStepper(issuingRfc)

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()

        viewMvc.registerListener(this)
        fragmentsEventBus.registerListener(this)
        dialogsEventBus.registerListener(this)
    }

    override fun onStop() {
        super.onStop()

        viewMvc.unregisterListener(this)
        fragmentsEventBus.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
    }

    override fun onNavigateUpClicked() {
        if (hasFormChanges) {
            dialogsManager.showExitWithoutSavingChanges(null)
        } else {
            screensNavigator.navigateUp()
        }
    }

    override fun onFragmentEvent(event: Any) {
        if (event is InvoiceChangeFragmentEvent) {
            hasFormChanges = true
        }
    }

    override fun onDialogEvent(event: Any) {
        if (event is PromptDialogEvent) {
            when (event.clickedButton) {
                PromptDialogEvent.Button.POSITIVE -> {
                    screensNavigator.navigateUp()
                }
                PromptDialogEvent.Button.NEGATIVE -> {
                    // Do nothing
                }
            }
        }
    }

    override fun onStepSelected() {
        // TODO:
    }

    override fun onReturnToPreviousStep() {
        // TODO:
    }

    override fun onStepError() {
        // TODO:
    }

    override fun onCompletedInvoice() {
        // TODO:
    }
}