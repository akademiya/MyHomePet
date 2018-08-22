package com.vadym.adv.myhomepet.ui.login.presenter

import android.app.Application
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter
import com.vadym.adv.myhomepet.isEmailAddressValid
import com.vadym.adv.myhomepet.ui.login.ISignupActivity
import com.vadym.adv.myhomepet.ui.login.view.SignupActivity

class SignupPresenter(signupActivity: SignupActivity, application: Application) : BasePresenter<SignupActivity>(signupActivity) {
    init { (application as AndroidApplication).applicationComponent.inject(this) }

    private var name = ""
    private var address = ""
    private var email = ""
    private var password = ""
    private var repassword = ""
    private var cb_gdpr = false
    private var isValidateSuccess = true

    override fun onBindView() {
        view?.setButtonCreateEnabled(isValidateSuccess)
    }
    override fun onUnbindView() {  }

    fun onValidateAndSave() {
        if (name.isEmpty()) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.NO_NAME)
            view?.setButtonCreateEnabled(false)
            isValidateSuccess = false
        }

        if (email.isEmpty()) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.NO_EMAIL)
            view?.setButtonCreateEnabled(false)
            isValidateSuccess = false
        } else if (!email.isEmailAddressValid()) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.INVALID_EMAIL)
            view?.setButtonCreateEnabled(false)
            isValidateSuccess = false
        }

        if (password.isEmpty()) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.NO_PASSWORD)
            view?.setButtonCreateEnabled(false)
            isValidateSuccess = false
        } else if (password.length < 4) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.PASSWORD_TOO_SHORT)
            view?.setButtonCreateEnabled(false)
            isValidateSuccess = false
        }

        if (repassword.length < 4 || repassword != password) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.REPASSWORD_WRONG)
            view?.setButtonCreateEnabled(false)
            isValidateSuccess = false
        }

        if (!cb_gdpr) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.CHECKBOX_DISABLE)
            view?.setButtonCreateEnabled(false)
            isValidateSuccess = false
        }
        if (isValidateSuccess) view?.onSignupSuccess()
    }

    fun onResetError() {
        isValidateSuccess = true
        view?.onResetError()
        view?.setButtonCreateEnabled(true)
    }

    fun onUpdateOwnerName(name: String) {
        this.name = name
    }
    fun onUpdateOwnerAddress(address: String) {
        this.address = address
    }
    fun onUpdateOwnerEmail(email: String) {
        this.email = email
    }
    fun onUpdateOwnerPassword(password: String) {
        this.password = password
    }
    fun onUpdateOwnerRePassword(repassword: String) {
        this.repassword = repassword
    }
    fun onUpdateCheckboxGdpr(isChecked: Boolean) {
        this.cb_gdpr = isChecked
    }
}