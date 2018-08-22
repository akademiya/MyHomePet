package com.vadym.adv.myhomepet.ui.login.presenter

import android.app.Application
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter
import com.vadym.adv.myhomepet.isEmailAddressValid
import com.vadym.adv.myhomepet.ui.login.ILoginActivity
import com.vadym.adv.myhomepet.ui.login.view.LoginActivity

class LoginPresenter(loginActivity: LoginActivity, application: Application) : BasePresenter<LoginActivity>(loginActivity) {
    init { (application as AndroidApplication).applicationComponent.inject(this) }

    private var email = ""
    private var password = ""
    private var isValidateSuccess = true

    override fun onBindView() {}
    override fun onUnbindView() {}

    fun onValidateAndLogin() {
        if (email.isEmpty()) {
            view?.showInvalidValue(ILoginActivity.InvalidValue.NO_EMAIL)
            view?.setButtonLoginEnabled(false)
            isValidateSuccess = false
        } else if (!email.isEmailAddressValid()) {
            view?.showInvalidValue(ILoginActivity.InvalidValue.INVALID_EMAIL)
            view?.setButtonLoginEnabled(false)
            isValidateSuccess = false
        }

        if (password.isEmpty()) {
            view?.showInvalidValue(ILoginActivity.InvalidValue.NO_PASSWORD)
            view?.setButtonLoginEnabled(false)
            isValidateSuccess = false
        } else if (password.length < 4) {
            view?.showInvalidValue(ILoginActivity.InvalidValue.PASSWORD_TOO_SHORT)
            view?.setButtonLoginEnabled(false)
            isValidateSuccess = false
        }
        if (isValidateSuccess) view?.onLoginSuccess()
    }

    fun onResetError() {
        isValidateSuccess = true
        view?.onResetError()
        view?.setButtonLoginEnabled(true)
    }

    fun onUpdateOwnerEmail(email: String) {
        this.email = email
    }

    fun onUpdateOwnerPassword(password: String) {
        this.password = password
    }
}