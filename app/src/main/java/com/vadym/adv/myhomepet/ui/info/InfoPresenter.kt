package com.vadym.adv.myhomepet.ui.info

import android.app.Application
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter

class InfoPresenter(infoView: InfoView, applicationComponent: Application) : BasePresenter<InfoView>(infoView) {
    init { (applicationComponent as AndroidApplication).applicationComponent.inject(this) }

    override fun onBindView() {
        view?.setButtonVisibility(true)
    }

    override fun onUnbindView() {}

}