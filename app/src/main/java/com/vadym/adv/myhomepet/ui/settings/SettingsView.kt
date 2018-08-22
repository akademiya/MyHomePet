package com.vadym.adv.myhomepet.ui.settings

import android.os.Bundle
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R

class SettingsView : BaseActivity(), ISettingsView {

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_info)
    }
}