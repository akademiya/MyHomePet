package com.vadym.adv.myhomepet.ui.settings

interface ISettingsView {
    fun updateNotification(isChecked: Boolean)
    fun updateOwnerName(name: String, onConfirm: () -> Unit)
    fun updateOwnerPhone(phone: String, onConfirm: () -> Unit)
    fun updateOwnerCity(city: String, onConfirm: () -> Unit)
    fun updateOwnerPassword(oldPassword: String, onConfirm: () -> Unit)
}