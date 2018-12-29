package com.vadym.adv.myhomepet.ui.main.presenter

import android.app.Application
import android.content.Intent
import android.net.Uri
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter
import com.vadym.adv.myhomepet.FlowActivity
import com.vadym.adv.myhomepet.ui.main.view.CardInfoView

class CardInfoPresenter(cardInfoView: CardInfoView, application: Application) : BasePresenter<CardInfoView>(cardInfoView) {
    init { (application as AndroidApplication).applicationComponent.inject(this) }

    override fun onBindView() {}

    override fun onUnbindView() {}

    fun onWriteMessageClick(email: String) {
        val sendIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        sendIntent.data = Uri.parse("mailto:$email")
        view?.onStartMessageWithOwner(sendIntent)
    }

    fun onBackToParent() {
        view?.goTo(FlowActivity.HOME)
    }
}