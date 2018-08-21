package com.vadym.adv.myhomepet.ui.login

import android.app.Application
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter
import com.vadym.adv.myhomepet.isEmailAddressValid

class SignupPresenter(signupActivity: SignupActivity, application: Application) : BasePresenter<SignupActivity>(signupActivity) {
    init { (application as AndroidApplication).applicationComponent.inject(this) }

    private var name = ""
    private var address = ""
    private var email = ""
    private var password = ""
    private var repassword = ""

    override fun onBindView() {}
    override fun onUnbindView() {}

    fun onValidateAndSave() {
        if (name.isEmpty()) view?.showInvalidValue(ISignupActivity.InvalidValue.NO_NAME)

        if (email.isEmpty()) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.NO_EMAIL)
        } else if (!email.isEmailAddressValid()) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.INVALID_EMAIL)
        }

        if (password.isEmpty()) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.NO_PASSWORD)
        } else if (password.length < 4) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.PASSWORD_TOO_SHORT)
        }

        if (repassword.length < 4 || repassword != password) {
            view?.showInvalidValue(ISignupActivity.InvalidValue.REPASSWORD_WRONG)
        }
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
}