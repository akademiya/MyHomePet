package com.vadym.adv.myhomepet.ui.settings

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.network.Endpoints.owner
import kotlinx.android.synthetic.main.view_settings.*
import java.security.acl.Owner

class SettingsView : BaseActivity(), ISettingsView {

    private lateinit var presenter: SettingsPresenter
    private var noFires = false

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_settings)
        presenter = SettingsPresenter(this, application)

        sw_notification.setOnCheckedChangeListener { _, isChecked ->
            if (!noFires) presenter.updateCheckedNotification(isChecked)
        }

        edit_owner_name.setOnClickListener { presenter.onEditName() }
        edit_owner_email.setOnClickListener { presenter.onEditEmail() }
        edit_owner_password.setOnClickListener { presenter.onEditPassword() }
        edit_owner_phone.setOnClickListener { presenter.onEditPhone() }
        edit_owner_city.setOnClickListener { presenter.onEdirCity() }
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

    override fun updateOwnerName() {
//        val inflater = LayoutInflater.from(baseContext)
//        val subView = inflater.inflate(R.layout.item_edit_data_owner, null)
//
//        val descriptionField = subView.findViewById<EditText>(R.id.owner_data)
//        descriptionField.setText(owner.personDescription)
//
//        val builder = AlertDialog.Builder(baseContext)
//        builder.setTitle(R.string.edit_data)
//        builder.setView(subView)
//        builder.create()
//        builder.setPositiveButton(R.string.edit_data) { _, _ ->
//            val description = descriptionField.text.toString()
//            database.updatePerson(Owner(owner.personId, description))
//
//            (baseContext as Activity).finish()
//            this.startActivity(this.intent)
//        }
//
//        builder.setNegativeButton(R.string.cancel) { _, _ -> Toast.makeText(this, R.string.task_cancelled, Toast.LENGTH_SHORT).show() }
//        builder.show()
    }

    private fun noFires(block: () -> Unit) {
        noFires = true
        block()
        noFires = false
    }
}