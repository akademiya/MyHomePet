package com.vadym.adv.myhomepet.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.ImageUtils
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.di.module.GlideApp
import com.vadym.adv.myhomepet.toAndroidVisibility
import com.vadym.adv.myhomepet.ui.main.ICardInfoView
import com.vadym.adv.myhomepet.ui.main.presenter.CardInfoPresenter
import kotlinx.android.synthetic.main.view_main_card_info.*

class CardInfoView : BaseActivity(), ICardInfoView {

    private lateinit var presenter: CardInfoPresenter

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_main_card_info)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        presenter = CardInfoPresenter(this, application)
        val intent = intent
        val category = intent.getStringExtra("petCategory")
        val action = intent.getStringExtra("action")
        val isSelectedPeriod = intent.getBooleanExtra("isSelectedPeriod", false)
        val period = intent.getStringExtra("period")
        val periodFrom = intent.getStringExtra("periodFrom")
        val periodTo = intent.getStringExtra("periodTo")
        val name = intent.getStringExtra("petName")
        val breed = intent.getStringExtra("breed")
        val age = intent.getStringExtra("age")
        val vaccine = intent.getStringExtra("vaccine")
        val description = intent.getStringExtra("description")
        val inventory = intent.getStringExtra("inventory")
        val photo = intent.getStringExtra("photo")
        val email = intent.getStringExtra("ownerEmail")

        info_category.text = category
        info_action.text = action
        info_container_period.visibility = isSelectedPeriod.toAndroidVisibility()
        info_period.text = period
        info_tv_day_from.text = periodFrom
        info_tv_day_to.text = periodTo
        info_pet_name.text = name
        info_breed.text = breed
        info_pet_age.text = age
        info_vaccine.text = vaccine
        info_description.text = description
        info_inventory.text = inventory

        button_back.setOnClickListener { presenter.onBackToParent() }
        write_message.setOnClickListener { presenter.onWriteMessageClick(email) }

        if (!photo.isBlank()) {
            GlideApp.with(this)
                    .load(ImageUtils.pathToReference(photo))
                    .centerCrop()
                    .into(photo_pet)
        } else photo_pet.visibility = View.GONE

        container_info_description.visibility = (!description.isBlank()).toAndroidVisibility()
        container_info_inventory.visibility = (!inventory.isBlank()).toAndroidVisibility()
    }

    override fun onStartMessageWithOwner(intent: Intent) {
        startActivity(Intent.createChooser(intent, "Lovely Home Animals"))
    }
}