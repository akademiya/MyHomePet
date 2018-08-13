package com.vadym.adv.myhomepet.ui.main

import android.app.Application
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter

class MainPresenter(mainView: MainActivity, applicationComponent: Application): BasePresenter<MainActivity>(mainView) {
    init { (applicationComponent as AndroidApplication).applicationComponent.inject(this) }

    override fun onBindView() {

    }

    override fun onUnbindView() {

    }
}