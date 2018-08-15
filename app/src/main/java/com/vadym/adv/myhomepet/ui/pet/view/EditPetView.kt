package com.vadym.adv.myhomepet.ui.pet.view

import android.annotation.SuppressLint
import android.os.Bundle
import com.vadym.adv.myhomepet.BaseActivity
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.data.SqliteDatabase
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.presenter.EditPetPresenter
import kotlinx.android.synthetic.main.view_my_pet_card_edit.*

class EditPetView : BaseActivity(), IEditPetView {

    private lateinit var presenter: EditPetPresenter
    private lateinit var database: SqliteDatabase

    @SuppressLint("RestrictedApi")
    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_my_pet_card_edit)
        presenter = EditPetPresenter(this, application)
        database = SqliteDatabase.getInstance(this)

        val name = "rr" //pet_name.text.toString()
//        val photo = 0 //resources.getDrawable(R.drawable.kisa2)
        val category = "Dog"
        val action = "full"
        val period = action_period.text.toString()
        val country = owner_country.text.toString()

        val petModel = PetModel(category, action, period, country)

        button_save.setOnClickListener {
            database.addPet(petModel)
            presenter.onBackToParent()
        }

    }
}