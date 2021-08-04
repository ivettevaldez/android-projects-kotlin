package com.ivettevaldez.saturnus.screens.common.dialogs.personselector

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.dialogs.BaseBottomSheetDialog
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import java.io.Serializable
import javax.inject.Inject

class PersonSelectorBottomSheetDialog(
    private val listener: IPersonSelectorBottomSheetViewMvc.Listener
) : BaseBottomSheetDialog() {

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    @Inject
    lateinit var controllerFactory: ControllerFactory

    private lateinit var controller: PersonSelectorBottomSheetController

    enum class PersonType : Serializable {

        RECEIVER
    }

    companion object {

        private const val ARG_PERSON_TYPE: String = "ARG_PERSON_TYPE"

        @JvmStatic
        fun newPersonSelectorBottomSheetDialog(
            personType: PersonType,
            listener: IPersonSelectorBottomSheetViewMvc.Listener
        ): PersonSelectorBottomSheetDialog {
            return PersonSelectorBottomSheetDialog(listener).apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PERSON_TYPE, personType)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        controller = controllerFactory.newPersonSelectorBottomSheetController(
            getPersonType(),
            listener
        )

        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewMvc = viewMvcFactory.newPersonSelectorBottomSheetViewMvc(null)
        val dialog = BottomSheetDialog(requireContext())

        controller.bindDialogAndView(dialog, viewMvc)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    override fun onStop() {
        super.onStop()
        controller.onStop()
    }

    private fun getPersonType(): PersonType =
        requireArguments().getSerializable(ARG_PERSON_TYPE) as PersonType
}