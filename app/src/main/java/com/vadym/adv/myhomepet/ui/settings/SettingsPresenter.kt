package com.vadym.adv.myhomepet.ui.settings

import android.app.Application
import com.google.firebase.database.*
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter


class SettingsPresenter(settingsView: SettingsView, application: Application) : BasePresenter<SettingsView>(settingsView) {
    init { (application as AndroidApplication).applicationComponent.inject(this) }

    private var hasNotification = false
    private lateinit var fbData: DatabaseReference

    override fun onBindView() {
        view?.updateNotification(hasNotification)
        fbData = FirebaseDatabase.getInstance().getReference("owner")
        fbData.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

            }

        })
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