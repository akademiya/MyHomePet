package com.vadym.adv.myhomepet.ui.settings

import android.app.Application
import android.content.Intent
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseAuth
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter
import com.vadym.adv.myhomepet.FirestoreUtils
import com.vadym.adv.myhomepet.FlowActivity


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
        view?.updateOwnerName(name, {})
    }
    fun onEditPassword(password: String) {
        this.password = password
        view?.updateOwnerPassword(password)
    }
    fun onEditPhone(phone: String) {
        view?.updateOwnerPhone(phone, {})
    }
    fun onEditCity(city: String) {
        view?.updateOwnerCity(city, {})
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        if (this.password == oldPassword) {
            auth?.currentUser?.updatePassword(newPassword)
            view?.onChangedPasswordSuccessful(newPassword)
        } else
            view?.onErrorEditPassword(ISettingsView.ErrorEditing.ERROR_PASSWORD) //ISettingsView.ErrorEditing.ERROR_PASSWORD
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