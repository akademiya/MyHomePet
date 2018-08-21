package com.vadym.adv.myhomepet.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.setSimpleTextWatcher
import com.vadym.adv.myhomepet.ui.main.MainActivity
import kotlinx.android.synthetic.main.view_signup.*

class SignupActivity : AppCompatActivity(), ISignupActivity {

    private var auth: FirebaseAuth? = null
    lateinit var presenter: SignupPresenter

    var name = ""
    var address = ""
    var email = ""
    var password = ""
    var reEnterPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.view_signup)
        presenter = SignupPresenter(this, application)

        auth = FirebaseAuth.getInstance()

        input_name.setSimpleTextWatcher {
            this.name = it.trim()
            presenter.onUpdateOwnerName(it)
        }
        input_address.setSimpleTextWatcher {
            this.address = it.trim()
            presenter.onUpdateOwnerAddress(it)
        }
        input_email.setSimpleTextWatcher {
            this.email = it.trim()
            presenter.onUpdateOwnerEmail(it)
        }
        input_password.setSimpleTextWatcher {
            this.password = it.trim()
            presenter.onUpdateOwnerPassword(it)
        }
        input_rePassword.setSimpleTextWatcher {
            this.reEnterPassword = it.trim()
            presenter.onUpdateOwnerRePassword(it)
        }

        if (TextUtils.isEmpty(email)) {

        }

        btn_signup.setOnClickListener {
            val progressDialog = ProgressDialog(this@SignupActivity)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage(resources.getString(R.string.signup_successful))
            progressDialog.show()

            presenter.onValidateAndSave()

            auth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this@SignupActivity) { task ->
                Toast.makeText(this@SignupActivity, "createUserWithEmail:onComplete:" + task.isSuccessful, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()

                if (!task.isSuccessful) {
                    Toast.makeText(this@SignupActivity, "Authentication failed." + task.exception!!, Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    finish()
                }
            }
        }

        link_login.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

        //        btn_signup.setOnClickListener { signup() }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unbindView(this)
    }

    override fun showInvalidValue(errorField: ISignupActivity.InvalidValue) {
        when(errorField) {
            ISignupActivity.InvalidValue.NO_NAME -> til_input_name.error = resources.getString(R.string.no_name)
            ISignupActivity.InvalidValue.NO_EMAIL -> til_input_email.error = resources.getString(R.string.no_email)
            ISignupActivity.InvalidValue.INVALID_EMAIL -> til_input_email.error = resources.getString(R.string.email_incorrect)
            ISignupActivity.InvalidValue.EMAIL_ALREADY_EXIST -> til_input_email.error = resources.getString(R.string.email_exist)
            ISignupActivity.InvalidValue.NO_PASSWORD -> til_input_password.error = resources.getString(R.string.no_password)
            ISignupActivity.InvalidValue.PASSWORD_TOO_SHORT -> til_input_password.error = resources.getString(R.string.password_too_short)
            ISignupActivity.InvalidValue.REPASSWORD_WRONG -> til_input_rePassword.error = resources.getString(R.string.repassword_wrong)
            ISignupActivity.InvalidValue.CHECKBOX_DISABLE -> resources.getString(R.string.checkbox_gdpr)
            ISignupActivity.InvalidValue.NO_INTERNET -> resources.getString(R.string.no_internet)
        }
    }

    fun signup() {
        Log.d(TAG, "Signup")

//        if (!validate()) {
//            onSignupFailed()
//            return
//        }

        btn_signup.isEnabled = false

        val progressDialog = ProgressDialog(this@SignupActivity) // TODO: , com.vadym.adv.myhomepet.R.style.AppTheme_Dark_Dialog
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        val name = input_name.text.toString()
        val address = input_address.text.toString()
//        val email = input_email.text.toString()
        val mobile = input_mobile.text.toString()
        val password = input_password.text.toString()
        val reEnterPassword = input_rePassword.text.toString()

        // TODO: Implement your own signup logic here.

        android.os.Handler().postDelayed(
                {
                    // On complete call either onSignupSuccess or onSignupFailed
                    // depending on success
                    onSignupSuccess()
                    // onSignupFailed();
                    progressDialog.dismiss()
                }, 3000)
    }


    private fun onSignupSuccess() {
        btn_signup.isEnabled = true
//        setResult(Activity.RESULT_OK, null)
//        finish()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun onSignupFailed() {
        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()

        btn_signup.isEnabled = true
    }

//    private fun validate(): Boolean {
//        var valid = true
//
//        val name = input_name.text.toString()
//        val address = input_address.text.toString()
////        val email = input_email.text.toString()
//        val mobile = input_mobile.text.toString()
//        val password = input_password.text.toString()
//        val rePassword = input_rePassword.text.toString()
//
//        if (name.isEmpty() || name.length < 3) {
//            input_name.error = "at least 3 characters"
//            valid = false
//        } else {
//            input_name.error = null
//        }
//
//        if (address.isEmpty()) {
//            input_address.error = "Enter Valid Address"
//            valid = false
//        } else {
//            input_address.error = null
//        }
//
//
////        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
////            input_email.error = "enter a valid email address"
////            valid = false
////        } else {
////            input_email.error = null
////        }
//
//        if (mobile.isEmpty() || mobile.length != 10) {
//            input_mobile.error = "Enter Valid Mobile Number"
//            valid = false
//        } else {
//            input_mobile.error = null
//        }
//
//        if (password.isEmpty() || password.length < 4 || password.length > 10) {
//            input_password.error = "between 4 and 10 alphanumeric characters"
//            valid = false
//        } else {
//            input_password.error = null
//        }
//
//        if (rePassword.isEmpty() || rePassword.length < 4 || rePassword.length > 10 || rePassword != password) {
//            input_rePassword.error = "Password Do not match"
//            valid = false
//        } else {
//            input_rePassword.error = null
//        }
//
//        return valid
//    }

    companion object {
        private val TAG = "SignupActivity"
    }
}