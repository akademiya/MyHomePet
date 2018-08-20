package com.vadym.adv.myhomepet.ui.pet.presenter

import android.app.Application
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter
import com.vadym.adv.myhomepet.FlowActivity
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.view.EditPetView

class EditPetPresenter(editPetView: EditPetView, application: Application) : BasePresenter<EditPetView>(editPetView) {

    init { (application as AndroidApplication).applicationComponent.inject(this) }

    private var period = ""
    private val param: PetModel? = null

    override fun onBindView() {
        view?.setCreateOrEditTitle(param?.id != null)
        view?.onDeleteItem(param?.id)
    }

    override fun onUnbindView() {}

    fun onBackToParent() {
        view?.goTo(FlowActivity.MY_PET)
    }

    fun updatePeriod(period: String) {
        this.period = period
    }

    fun updateData() {
        view?.updateAllData(period)
    }
}