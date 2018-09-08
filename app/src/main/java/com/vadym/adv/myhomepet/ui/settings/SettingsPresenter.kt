package com.vadym.adv.myhomepet.ui.settings

import android.app.Application
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter

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

}