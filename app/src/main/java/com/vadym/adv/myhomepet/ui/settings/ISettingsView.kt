package com.vadym.adv.myhomepet.ui.settings

import android.content.Intent

interface ISettingsView {
    fun updateNotification(isChecked: Boolean)
    fun updateOwnerName(name: String)
    fun updateOwnerPhone(phone: String)
    fun updateOwnerCity(city: String)
    fun updateOwnerPassword(ownerOldPassword: String)
    fun onSaveDataSettings()
    fun onSelectImageInAlbum(intent: Intent)
    fun onErrorDataChanged(error: ErrorEditing)
    fun onChangedPasswordSuccessful(newPassword: String)
    fun onChangedNameSuccessful(newName: String)
    fun onChangedNumberPhoneSuccessful(newNumber: String)

    enum class ErrorEditing {
        ERROR_PASSWORD,
        ERROR_NAME,
        ERROR_PHONE
    }
}