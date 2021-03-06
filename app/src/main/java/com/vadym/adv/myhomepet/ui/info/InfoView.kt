package com.vadym.adv.myhomepet.ui.info

import android.os.Bundle
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R

class InfoView : BaseActivity(), IInfoView {

    private lateinit var presenter: InfoPresenter

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_info)
        presenter = InfoPresenter(this, application)

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unbindView(this)
    }

    override fun setButtonVisibility(isVisible: Boolean) {
//        btn_info.visibility = isVisible.toAndroidVisibility()
    }

}