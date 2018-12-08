package com.vadym.adv.myhomepet.ui.settings

import android.content.Intent

interface ISettingsView {
    fun updateNotification(isChecked: Boolean)
    fun updateOwnerName(name: String, onConfirm: () -> Unit)
    fun updateOwnerPhone(phone: String, onConfirm: () -> Unit)
    fun updateOwnerCity(city: String, onConfirm: () -> Unit)
    fun updateOwnerPassword(ownerOldPassword: String)
    fun onSaveDataSettings()
    fun onSelectImageInAlbum(intent: Intent)
    fun onErrorEditPassword(error: ErrorEditing) //error: ErrorEditing
    fun onChangedPasswordSuccessful(newPassword: String)
    fun onCityChanged(city: String)

    enum class ErrorEditing {
        ERROR_PASSWORD,
        ERROR_NAME,
        ERROR_PHONE,
        ERROR_CITY
    }
}