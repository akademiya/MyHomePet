package com.vadym.adv.myhomepet.ui.info

import android.os.Bundle
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.setSimpleTextWatcher
import com.vadym.adv.myhomepet.toAndroidVisibility
import kotlinx.android.synthetic.main.view_info.*

class InfoView : BaseActivity(), IInfoView {

    private lateinit var presenter: InfoPresenter

    override fun setLayout(): Int { return R.layout.view_info }

    override fun init(savedInstanceState: Bundle?) {
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
        btn_info.visibility = isVisible.toAndroidVisibility()
    }

}