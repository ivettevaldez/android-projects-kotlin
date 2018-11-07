package com.silviavaldez.sampleapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.AnimationHelper
import com.silviavaldez.sampleapp.helpers.PreferencesHelper
import com.silviavaldez.sampleapp.helpers.UtilHelper
import com.silviavaldez.sampleapp.services.delegates.ISignInDelegate
import com.silviavaldez.sampleapp.services.rest.SignInService
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

private const val MIN_LENGTH: Int = 6

class LoginActivity : AppCompatActivity(), ISignInDelegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setListenerToLoginButton()
    }

    override fun onBackPressed() {
        finish()
        AnimationHelper().exitTransition(this)
        super.onBackPressed()
    }

    override fun onSignInSuccess() {
        PreferencesHelper(this).login()
        showProgress(false)
        goToDashboard()
    }

    override fun onSignInFailure(error: String) {
        showProgress(false)
        showMessage(error, Snackbar.LENGTH_LONG)
    }

    private fun getJsonCredentials(email: String, password: String): String {
        return JSONObject()
                .put("email", email)
                .put("password", password)
                .toString()
    }

    private fun setListenerToLoginButton() {
        login_button_login.setOnClickListener {
            Thread {
                val userName = login_auto_user_name.text.toString().trim()
                val password = login_edit_password.text.toString().trim()

                val validCredentials = validateCredentials(userName, password)

                if (validCredentials) {
                    val jsonCredentials = getJsonCredentials(userName, password)
                    val signingIn = SignInService(this).signIn(jsonCredentials)

                    if (signingIn) {
                        showProgress(true)
                    } else {
                        showMessage(R.string.error_msg_no_internet_connection, Snackbar.LENGTH_LONG)
                    }
                }
            }.start()
        }
    }

    private fun enableViews(enabled: Boolean) {
        login_auto_user_name.isEnabled = enabled
        login_edit_password.isEnabled = enabled
        login_button_login.isEnabled = enabled
    }

    private fun showProgress(show: Boolean) {
        runOnUiThread {
            UtilHelper().showView(login_progress, show)
            enableViews(!show)
        }
    }

    private fun validateCredentials(userName: String, password: String): Boolean {
        return validateUserName(userName) && validatePassword(password)
    }

    private fun validateUserName(userName: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9]*$")
        when {
            userName.isEmpty() -> {
                showMessage(R.string.error_empty_username, Snackbar.LENGTH_SHORT)
                return false
            }
            userName.length < MIN_LENGTH -> {
                showMessage(R.string.error_unauthorized, Snackbar.LENGTH_SHORT)
                return false
            }
            userName.contains("@") -> {
                showMessage(R.string.error_wrong_user_name, Snackbar.LENGTH_LONG)
                return false
            }
            !userName.matches(regex = regex) -> {
                showMessage(R.string.error_wrong_user_name, Snackbar.LENGTH_LONG)
                return false
            }
        }
        return true
    }

    private fun validatePassword(pass: String): Boolean {
        when {
            pass.isEmpty() -> {
                showMessage(R.string.error_empty_password, Snackbar.LENGTH_SHORT)
                return false
            }
            pass.length < MIN_LENGTH -> {
                showMessage(R.string.error_unauthorized, Snackbar.LENGTH_SHORT)
                return false
            }
        }
        return true
    }

    private fun goToDashboard() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        AnimationHelper().enterTransition(this)
    }

    private fun showMessage(message: Int, length: Int) {
        runOnUiThread {
            Snackbar.make(login_layout, message, length).show()
        }
    }

    private fun showMessage(message: String, length: Int) {
        runOnUiThread {
            Snackbar.make(login_layout, message, length).show()
        }
    }
}
