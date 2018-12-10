package com.vadym.adv.myhomepet.ui.settings

import android.app.Application
import android.content.Intent
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseAuth
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter


class SettingsPresenter(settingsView: SettingsView, application: Application) : BasePresenter<SettingsView>(settingsView) {
    init { (application as AndroidApplication).applicationComponent.inject(this) }

    private var hasNotification = false
    private var auth: FirebaseAuth? = null
    private var password: String = ""

    override fun onBindView() {
        view?.updateNotification(hasNotification)
        auth = FirebaseAuth.getInstance()
    }

    override fun onUnbindView() { }

    fun updateCheckedNotification(isChecked: Boolean) {
        hasNotification = isChecked
    }

    fun onEditName(name: String) {
        view?.updateOwnerName(name)
    }
    fun onEditPassword(password: String) {
        this.password = password
        view?.updateOwnerPassword(password)
    }
    fun onEditPhone(phone: String) {
        view?.updateOwnerPhone(phone)
    }
    fun onEditCity(city: String) {
        view?.updateOwnerCity(city)
    }

    fun updateName(newName: String) {
        if (newName.isNotBlank()) {
            view?.onChangedNameSuccessful(newName)
        } else
            view?.onErrorDataChanged(ISettingsView.ErrorEditing.ERROR_NAME)
    }

    fun updateNumberPhone(newNumber: String) {
        if (newNumber.isNotBlank()) { // TODO validate phone number
            view?.onChangedNumberPhoneSuccessful(newNumber)
        } else
            view?.onErrorDataChanged(ISettingsView.ErrorEditing.ERROR_PHONE)
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        if (this.password == oldPassword) {
            auth?.currentUser?.updatePassword(newPassword)
            view?.onChangedPasswordSuccessful(newPassword)
        } else
            view?.onErrorDataChanged(ISettingsView.ErrorEditing.ERROR_PASSWORD)
    }

    fun onSelectImageChecked() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png", "image/jpg"))
        view?.onSelectImageInAlbum(intent)
    }

    fun onSaveButtonClick() {
        view?.onSaveDataSettings()
    }

    fun onSignOutClick() {
        auth?.signOut()
        view?.signOut()
    }

}