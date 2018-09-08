package com.vadym.adv.myhomepet.ui.settings

import android.os.Bundle
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R
import kotlinx.android.synthetic.main.view_settings.*

class SettingsView : BaseActivity(), ISettingsView {

    private lateinit var presenter: SettingsPresenter
    private var noFires = false

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_settings)
        presenter = SettingsPresenter(this, application)

        sw_notification.setOnCheckedChangeListener { _, isChecked ->
            if (!noFires) presenter.updateCheckedNotification(isChecked)
        }
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

    private fun noFires(block: () -> Unit) {
        noFires = true
        block()
        noFires = false
    }
}