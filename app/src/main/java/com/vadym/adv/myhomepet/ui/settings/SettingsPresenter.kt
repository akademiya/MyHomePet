package com.vadym.adv.myhomepet.ui.settings

import android.app.Application
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter
import com.vadym.adv.myhomepet.domain.interactor.owner.GetOwnerDataCase
// private val getOwnerDataCase: GetOwnerDataCase,
class SettingsPresenter(settingsView: SettingsView, application: Application) : BasePresenter<SettingsView>(settingsView) {
    init { (application as AndroidApplication).applicationComponent.inject(this) }

    private var hasNotification = false

    override fun onBindView() {
        view?.updateNotification(hasNotification)
    }

    override fun onUnbindView() { }

    fun updateCheckedNotification(isChecked: Boolean) {
        hasNotification = isChecked
    }

    fun onEditName(name: String) {
        view?.updateOwnerName(name, {})
    }
    fun onEditPassword(password: String) {
        view?.updateOwnerPassword(password, {})
    }
    fun onEditPhone(phone: String) {
        view?.updateOwnerPhone(phone, {})
    }
    fun onEditCity(city: String) {
        view?.updateOwnerCity(city, {})
    }

}