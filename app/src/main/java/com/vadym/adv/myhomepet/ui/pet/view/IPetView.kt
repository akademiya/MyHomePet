package com.vadym.adv.myhomepet.ui.pet.view

interface IPetView {
    fun showEmptyList(isVisible: Boolean)
    fun showListPets(isVisible: Boolean)
    fun onEditCardPet(param: Int?)
}