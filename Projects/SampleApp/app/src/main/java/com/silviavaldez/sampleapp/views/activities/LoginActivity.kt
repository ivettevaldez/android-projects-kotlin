package com.silviavaldez.sampleapp.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.silviavaldez.sampleapp.helpers.AnimationHelper
import com.silviavaldez.sampleapp.helpers.PreferencesHelper
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.UtilHelper
import kotlinx.android.synthetic.main.activity_login.*

private const val MIN_LENGTH: Int = 6
private const val DELAY: Long = 1500L

class LoginActivity : AppCompatActivity() {

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

    private fun setListenerToLoginButton() {
        login_button_login.setOnClickListener {
            showProgress(true)

            val userName = login_auto_user_name.text.toString().trim()
            val password = login_edit_password.text.toString().trim()

            val validCredentials = validateCredentials(userName, password)

            if (validCredentials) {
                PreferencesHelper(this).login()

                Handler().postDelayed({
                    showProgress(false)
                    goToDashboard()
                }, DELAY)
            } else {
                showProgress(false)
            }
        }
    }

    private fun enableViews(enabled: Boolean) {
        login_auto_user_name.isEnabled = enabled
        login_edit_password.isEnabled = enabled
        login_button_login.isEnabled = enabled
    }

    private fun showProgress(show: Boolean) {
        UtilHelper().showView(login_progress, show)
        enableViews(!show)
    }

    private fun validateCredentials(userName: String, password: String): Boolean {
        return validateUserName(userName) && validatePassword(password)
    }

    private fun validateUserName(userName: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9]*$")
        when {
            userName.isEmpty() -> {
                Snackbar.make(login_layout_container,
                        R.string.error_empty_username,
                        Snackbar.LENGTH_SHORT).show()
                return false
            }
            userName.length < MIN_LENGTH -> {
                Snackbar.make(login_layout_container,
                        R.string.error_unauthorized,
                        Snackbar.LENGTH_SHORT).show()
                return false
            }
            userName.contains("@") -> {
                Snackbar.make(login_layout_container,
                        R.string.error_wrong_user_name,
                        Snackbar.LENGTH_LONG).show()
                return false
            }
            !userName.matches(regex = regex) -> {
                Snackbar.make(login_layout_container,
                        R.string.error_wrong_user_name,
                        Snackbar.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    private fun validatePassword(pass: String): Boolean {
        when {
            pass.isEmpty() -> {
                Snackbar.make(login_layout_container,
                        R.string.error_empty_password,
                        Snackbar.LENGTH_SHORT).show()
                return false
            }
            pass.length < MIN_LENGTH -> {
                Snackbar.make(login_layout_container,
                        R.string.error_unauthorized,
                        Snackbar.LENGTH_SHORT).show()
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
}
