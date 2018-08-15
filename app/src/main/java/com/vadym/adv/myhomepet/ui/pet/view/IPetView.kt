package com.vadym.adv.myhomepet.ui.pet.view

interface IPetView {
    fun setTitleVisibility(isVisible: Boolean)
    fun showEmptyList(isVisible: Boolean)
    fun showListPets(isVisible: Boolean)
}