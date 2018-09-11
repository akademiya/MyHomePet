package com.vadym.adv.myhomepet.ui.settings

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.domain.Owner
import com.vadym.adv.myhomepet.showDialogEditDataOwner
import kotlinx.android.synthetic.main.view_settings.*

class SettingsView : BaseActivity(), ISettingsView {

    private lateinit var presenter: SettingsPresenter
    private var noFires = false
    private val owner: Owner = Owner()

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_settings)
        presenter = SettingsPresenter(this, application)

        sw_notification.setOnCheckedChangeListener { _, isChecked ->
            if (!noFires) presenter.updateCheckedNotification(isChecked)
        }

        edit_owner_name.setOnClickListener { presenter.onEditName(owner.name) }
        edit_owner_phone.setOnClickListener { presenter.onEditPhone(owner.phone) }
        edit_owner_city.setOnClickListener { presenter.onEditCity(owner.city) }
        edit_owner_password.setOnClickListener { presenter.onEditPassword(owner.pin) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        presenter.unbindView(this)
        super.onDetachedFromWindow()
    }

    override fun updateNotification(isChecked: Boolean) = noFires {
        sw_notification.isChecked = isChecked
    }

    override fun updateOwnerName(name: String, onConfirm: () -> Unit) {
        this.showDialogEditDataOwner(resources.getString(R.string.change_owner_name), name, {}, { onConfirm() })
    }

    override fun updateOwnerCity(city: String, onConfirm: () -> Unit) {
        this.showDialogEditDataOwner(resources.getString(R.string.change_owner_city), city, {}, { onConfirm() })
    }

    override fun updateOwnerPhone(phone: String, onConfirm: () -> Unit) {
        this.showDialogEditDataOwner(resources.getString(R.string.change_owner_phone), phone, {}, { onConfirm() })
    }

    override fun updateOwnerPassword(oldPassword: String, onConfirm: () -> Unit) {
        val inflater = LayoutInflater.from(baseContext)
        val subView = inflater.inflate(R.layout.item_edit_data_owner, null)

        val descriptionField = subView.findViewById<EditText>(R.id.owner_data)
        descriptionField.setText(oldPassword)

        AlertDialog.Builder(baseContext).run {
            setTitle(R.string.change_owner_password)
            setView(subView)
            create()
            setPositiveButton(R.string.change) { _, _ ->
                val description = descriptionField.text.toString()
//            database.updatePerson(Owner(owner.pin, description))

                (baseContext as Activity).finish()
                startActivity(intent)
            }
            setNegativeButton(R.string.cancel) { _, _ -> }
            show()
        }
    }

    private fun noFires(block: () -> Unit) {
        noFires = true
        block()
        noFires = false
    }
}