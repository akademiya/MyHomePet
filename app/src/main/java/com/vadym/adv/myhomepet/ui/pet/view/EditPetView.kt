package com.vadym.adv.myhomepet.ui.pet.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.vadym.adv.myhomepet.*
import com.vadym.adv.myhomepet.R.string.action
import com.vadym.adv.myhomepet.R.string.category
import com.vadym.adv.myhomepet.data.SqliteDatabase
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.presenter.EditPetPresenter
import kotlinx.android.synthetic.main.view_my_pet_card_edit.*

class EditPetView : BaseActivity(), IEditPetView {

    private lateinit var presenter: EditPetPresenter
    private lateinit var database: SqliteDatabase
    private var period = ""
    private var country = ""


//    @SuppressLint("RestrictedApi")
//    private val noPhotoDrawable = AppCompatDrawableManager.get().getDrawable(baseContext, R.drawable.ic_nophoto).apply { setColorFilter(baseContext.resources.getColor(R.color.ic_normal_dark), PorterDuff.Mode.MULTIPLY) }


    @SuppressLint("RestrictedApi")
    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_my_pet_card_edit)
        presenter = EditPetPresenter(this, application)
        database = SqliteDatabase.getInstance(this)

        btn_choose_photo.setOnClickListener { applicationContext.startTakeGalleryImageIntent() }
        btn_take_photo.setOnClickListener { applicationContext.startCaptureCameraImageIntent() }
        btn_remove_photo.setOnClickListener { updateImageRepresentation("") }

        val name = "rr" //pet_name.text.toString()
//        val photo = 0 //resources.getDrawable(R.drawable.kisa2)
        val category = "Dog"
        val action = "full"

        action_period.setSimpleTextWatcher { presenter.updatePeriod(it) }
        owner_country.setSimpleTextWatcher { presenter.updateCountry(it) }

        button_save.setOnClickListener {
            presenter.updateData()
            database.addPet(PetModel(category, action, this.period, this.country))
            presenter.onBackToParent()
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unbindView(this)
    }

    override fun updateAllData(period: String, country: String) {
        this.period = period
        this.country = country
    }

    private fun updateImageRepresentation(src: String) {
        if (src.isBlank()) {
            iv_pet_image.setImageResource(R.drawable.ic_nophoto)
            iv_pet_image.setColorFilter(resources.getColor(R.color.ic_normal_dark))
            btn_remove_photo.visibility = View.GONE
        } else {
            iv_pet_image.clearColorFilter()
            btn_remove_photo.visibility = View.VISIBLE
//            GlideApp.with(baseContext)
//                    .load(src)
//                    .fallback(noPhotoDrawable)
//                    .error(noPhotoDrawable)
//                    .centerCrop()
//                    .into(iv_pet_image)
        }
    }
}