package com.vadym.adv.myhomepet.ui.main

import android.os.Bundle
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R

class MainActivity : BaseActivity() {

    private lateinit var presenter: MainPresenter

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.activity_main)
        presenter = MainPresenter(this, application)

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unbindView(this)
    }
}