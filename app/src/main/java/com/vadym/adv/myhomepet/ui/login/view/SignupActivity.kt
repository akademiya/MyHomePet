package com.vadym.adv.myhomepet.ui.login.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.vadym.adv.myhomepet.*
import com.vadym.adv.myhomepet.service.FirebaseOwnerIDService
import com.vadym.adv.myhomepet.ui.login.ISignupActivity
import com.vadym.adv.myhomepet.ui.login.presenter.SignupPresenter
import com.vadym.adv.myhomepet.ui.main.view.MainActivity
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.CITY_KEY
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.EMAIL_KEY
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.NAME_KEY
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.PHONE_KEY
import com.vadym.adv.myhomepet.ui.settings.SettingsView.Companion.PIN_KEY
import kotlinx.android.synthetic.main.view_signup.*

class SignupActivity : AppCompatActivity(), ISignupActivity {

    private var auth: FirebaseAuth? = null
    private var city = ""
    private var cityID: Int = 0
    lateinit var presenter: SignupPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.view_signup)
        presenter = SignupPresenter(this, application)

        auth = FirebaseAuth.getInstance()

        input_name.setFocusableWatcher()
        input_email.setFocusableWatcher()
        input_password.setFocusableWatcher()
        input_rePassword.setFocusableWatcher()

        input_name.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.onUpdateOwnerName(it)
        }
        input_email.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.onUpdateOwnerEmail(it)
        }
        input_password.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.onUpdateOwnerPassword(it)
        }
        input_rePassword.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.onUpdateOwnerRePassword(it)
        }
        checkbox_gdpr.setOnCheckedChangeListener { _, isChecked ->
            presenter.onResetError()
            presenter.onUpdateCheckboxGdpr(isChecked)
        }

        btn_signup.setOnClickListener {
            presenter.onValidateAndSave()
        }

        link_login.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

        val spinnerCountryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.city_owner,
                R.layout.spinner_simple_item
        )
        spinnerCountryAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        input_owner_city.adapter = spinnerCountryAdapter
        input_owner_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cityID = position
                city = spinnerCountryAdapter.getItem(position).toString()
            }
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

    override fun setButtonCreateEnabled(enable: Boolean) {
        btn_signup.isEnabled = enable
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
            ISignupActivity.InvalidValue.CHECKBOX_DISABLE -> checkbox_gdpr_error.text = resources.getString(R.string.checkbox_gdpr)
            ISignupActivity.InvalidValue.NO_INTERNET -> resources.getString(R.string.no_internet)
        }
    }

    override fun onResetError() {
        til_input_name.error = ""
        til_input_email.error = ""
        til_input_password.error = ""
        til_input_rePassword.error = ""
        checkbox_gdpr_error.text = ""
    }

    override fun onSignupSuccess() {
        hideKeyboard()
        val progressDialog = ProgressDialog(this@SignupActivity)
        val email = input_email.text.toString().trim()
        val name = input_name.text.toString().trim()
        val password = input_password.text.toString().trim()
        val city = this.city
        val phone = resources.getString(R.string.empty_phone)

        auth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this@SignupActivity) { task ->
            if (task.isSuccessful) {
                progressDialog.isIndeterminate = true
                progressDialog.setMessage(resources.getString(R.string.signup_successful))
                progressDialog.show()

                if (email.isBlank() || password.isBlank() || name.isBlank() || city.isBlank()) { return@addOnCompleteListener }
                val dataToSave = HashMap<String, Any>()
                dataToSave[NAME_KEY] = name
                dataToSave[EMAIL_KEY] = email
                dataToSave[PIN_KEY] = password
                dataToSave[CITY_KEY] = city
                dataToSave[PHONE_KEY] = phone
                FirestoreUtils.currentUserDocRef.set(dataToSave)

                FirestoreUtils.initCurrentUserIfFirstTime {
                    FirebaseOwnerIDService.addTokenToFirestore(FirebaseInstanceId.getInstance().token)
                    progressDialog.dismiss()
                    startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    finish()
                }
            } // TODO if(noInternetConnection) Exception
        }
    }

}