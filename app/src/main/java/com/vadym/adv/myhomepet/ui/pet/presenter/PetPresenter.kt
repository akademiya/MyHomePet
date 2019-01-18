package com.vadym.adv.myhomepet.ui.pet.presenter

import android.app.Application
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.view.PetView

class PetPresenter(petView: PetView, applicationComponent: Application) : BasePresenter<PetView>(petView) {
    init { (applicationComponent as AndroidApplication).applicationComponent.inject(this) }

    override fun onBindView() {

    }

    override fun onUnbindView() {}



    fun goToAddNewPosition() { view?.onCreateCardPet() }

    fun onEditCardItem(petModel: PetModel, position: Int) {
//        view?.onEditCardPet(petModel.id)
    }

    fun onPetListVisibility(emptyList: Boolean) {
        view?.showEmptyList(!emptyList)
        view?.showListPets(emptyList)
    }

}