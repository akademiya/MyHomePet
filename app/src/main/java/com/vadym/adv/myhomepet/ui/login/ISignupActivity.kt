package com.vadym.adv.myhomepet.ui.login

interface ISignupActivity {
    fun showInvalidValue(errorField: InvalidValue)

    enum class InvalidValue {
        NO_NAME,
        NO_EMAIL,
        INVALID_EMAIL,
        EMAIL_ALREADY_EXIST,
        NO_PASSWORD,
        PASSWORD_TOO_SHORT,
        REPASSWORD_WRONG,
        NO_INTERNET,
        CHECKBOX_DISABLE
    }
}