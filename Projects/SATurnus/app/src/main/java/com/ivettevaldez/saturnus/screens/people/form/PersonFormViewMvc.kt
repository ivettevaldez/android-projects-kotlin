package com.ivettevaldez.saturnus.screens.people.form

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.ivettevaldez.saturnus.R
import com.ivettevaldez.saturnus.people.Person
import com.ivettevaldez.saturnus.people.clienttype.ClientType
import com.ivettevaldez.saturnus.screens.common.fields.ISimpleTextInputViewMvc
import com.ivettevaldez.saturnus.screens.common.fields.ISpinnerInputViewMvc
import com.ivettevaldez.saturnus.screens.common.toolbar.IToolbarViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.BaseObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.IObservableViewMvc
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory

interface IPersonFormViewMvc : IObservableViewMvc<IPersonFormViewMvc.Listener> {

    interface Listener {

        fun onNavigateUpClicked()
        fun onRfcChanged(rfc: String)
        fun onSaveClicked()
    }

    fun bindPerson(person: Person)
    fun getName(): String
    fun getRfc(): String
    fun getPersonType(): String
    fun getClientType(): String
    fun cleanFields()
    fun showProgressIndicator()
    fun hideProgressIndicator()
    fun setToolbarTitle(title: String)
    fun setPersonType(type: String)
}

class PersonFormViewMvcImpl(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<IPersonFormViewMvc.Listener>(
    inflater,
    parent,
    R.layout.layout_person_form
), IPersonFormViewMvc {

    private val layoutProgress: FrameLayout = findViewById(R.id.person_form_progress)
    private val buttonSave: Button = findViewById(R.id.button_primary)

    private val toolbarContainer: Toolbar = findViewById(R.id.person_form_toolbar)
    private val toolbar: IToolbarViewMvc = viewMvcFactory.newToolbarViewMvc(toolbarContainer)

    private val inputNameContainer: FrameLayout = findViewById(R.id.person_form_input_name)
    private val inputName: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputNameContainer)

    private val inputRfcContainer: FrameLayout = findViewById(R.id.person_form_input_rfc)
    private val inputRfc: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputRfcContainer)

    private val inputPersonTypeContainer: FrameLayout =
        findViewById(R.id.person_form_input_person_type)
    private val inputPersonType: ISimpleTextInputViewMvc =
        viewMvcFactory.newSimpleTextInputViewMvc(inputPersonTypeContainer)

    private val inputClientTypeContainer: FrameLayout =
        findViewById(R.id.person_form_input_client_type)
    private val inputClientType: ISpinnerInputViewMvc =
        viewMvcFactory.newSpinnerInputViewMvc(inputClientTypeContainer)

    init {

        toolbarContainer.addView(toolbar.getRootView())

        initFields()
        setListenerEvents()
    }

    override fun bindPerson(person: Person) {
        inputName.setText(person.name)
        inputRfc.setText(person.rfc)
        inputPersonType.setText(person.personType)
        inputClientType.setSelection(ClientType.getPosition(person.clientType))
    }

    override fun getName(): String = inputName.getText()

    override fun getRfc(): String = inputRfc.getText()

    override fun getPersonType(): String = inputPersonType.getText()

    override fun getClientType(): String = inputClientType.getText()

    override fun cleanFields() {
        inputName.clean()
        inputRfc.clean()
        inputPersonType.clean()
        inputClientType.clean()

        buttonSave.requestFocus()
        buttonSave.isEnabled = false
    }

    override fun showProgressIndicator() {
        layoutProgress.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        layoutProgress.visibility = View.GONE
    }

    override fun setToolbarTitle(title: String) {
        toolbar.setTitle(title)
    }

    override fun setPersonType(type: String) {
        inputPersonType.setText(type)
    }

    private fun initFields() {
        // Name field
        inputName.setHint(context.getString(R.string.people_name))
        inputName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
        inputNameContainer.addView(inputName.getRootView())

        // RFC field
        inputRfc.setHint(context.getString(R.string.people_rfc))
        inputRfc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS)
        inputRfc.setImeOptions(EditorInfo.IME_ACTION_DONE)
        inputRfcContainer.addView(inputRfc.getRootView())

        // PersonType field
        inputPersonType.setHint(context.getString(R.string.people_type))
        inputPersonType.disable()
        inputPersonTypeContainer.addView(inputPersonType.getRootView())

        // ClientType field
        inputClientType.setHint(context.getString(R.string.people_client_type))
        inputClientType.bindValues(R.array.people_client_types)
        inputClientTypeContainer.addView(inputClientType.getRootView())
    }

    private fun setListenerEvents() {
        toolbar.enableNavigateUpAndListen(object : IToolbarViewMvc.NavigateUpClickListener {
            override fun onNavigateUpClicked() {
                for (listener in listeners) {
                    listener.onNavigateUpClicked()
                }
            }
        })

        buttonSave.setOnClickListener {
            for (listener in listeners) {
                listener.onSaveClicked()
            }
        }

        inputRfc.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    for (listener in listeners) {
                        listener.onRfcChanged(getRfc())
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            }
        )
    }
}