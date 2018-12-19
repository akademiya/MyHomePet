package com.vadym.adv.myhomepet.ui.login.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.hideKeyboard
import com.vadym.adv.myhomepet.setFocusableWatcher
import com.vadym.adv.myhomepet.setSimpleTextWatcher
import com.vadym.adv.myhomepet.ui.login.ILoginActivity
import com.vadym.adv.myhomepet.ui.login.presenter.LoginPresenter
import com.vadym.adv.myhomepet.ui.main.MainActivity
import kotlinx.android.synthetic.main.view_login.*

class LoginActivity : AppCompatActivity(), ILoginActivity {

    private lateinit var presenter: LoginPresenter
    private var auth: FirebaseAuth? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_login)
        presenter = LoginPresenter(this, application)
        auth = FirebaseAuth.getInstance()

        input_email.setFocusableWatcher()
        input_password.setFocusableWatcher()

        input_email.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.onUpdateOwnerEmail(it)
        }
        input_password.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.onUpdateOwnerPassword(it)
        }

        btn_login.setOnClickListener { presenter.onValidateAndLogin() }

        link_signup.setOnClickListener {
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivityForResult(intent, 0)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unbindView(this)
    }

    override fun onResetError() {
        til_input_email.error = ""
        til_input_password.error = ""
    }

    override fun setButtonLoginEnabled(isEnable: Boolean) {
        btn_login.isEnabled = isEnable
    }

    override fun showInvalidValue(errorField: ILoginActivity.InvalidValue) {
        when(errorField) {
            ILoginActivity.InvalidValue.NO_EMAIL -> til_input_email.error = resources.getString(R.string.no_email)
            ILoginActivity.InvalidValue.INVALID_EMAIL -> til_input_email.error = resources.getString(R.string.email_incorrect)
            ILoginActivity.InvalidValue.NO_PASSWORD -> til_input_password.error = resources.getString(R.string.no_password)
            ILoginActivity.InvalidValue.PASSWORD_TOO_SHORT -> til_input_password.error = resources.getString(R.string.password_too_short)
        }
    }

    override fun onLoginSuccess() {
        hideKeyboard()
        val progressDialog = ProgressDialog(this@LoginActivity) // TODO: I can add style
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Login...")
        progressDialog.show()

        auth?.signInWithEmailAndPassword(input_email.text.toString(), input_password.text.toString())?.addOnCompleteListener(this@LoginActivity) { task ->
            if (task.isSuccessful) {
                if (auth?.currentUser != null) {
                    progressDialog.dismiss()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            } else {
                progressDialog.dismiss()
                if (auth?.currentUser?.email != input_email.text.toString().trim()) {
                    Toast.makeText(this, R.string.unregister_user, Toast.LENGTH_SHORT).show()
                } else if (auth?.currentUser?.uid != input_password.text.toString()) {
                    Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}