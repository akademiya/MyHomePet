package com.vadym.adv.myhomepet.ui.main.view

import android.content.Intent
import android.os.Bundle
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.ui.main.ICardInfoView
import com.vadym.adv.myhomepet.ui.main.presenter.CardInfoPresenter
import kotlinx.android.synthetic.main.view_main_card_info.*

class CardInfoView : BaseActivity(), ICardInfoView {

    private lateinit var presenter: CardInfoPresenter
    private var email = "gvd@gmail.com"

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_main_card_info)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        presenter = CardInfoPresenter(this, application)

        button_back.setOnClickListener { presenter.onBackToParent() }
        write_message.setOnClickListener { presenter.onWriteMessageClick(email) }
    }

    override fun onStartMessageWithOwner(intent: Intent) {
        startActivity(Intent.createChooser(intent, "Lovely Home Animals"))
    }
}