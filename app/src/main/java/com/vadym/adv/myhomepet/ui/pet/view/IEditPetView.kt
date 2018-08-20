package com.vadym.adv.myhomepet.ui.pet.view

interface IEditPetView {
    fun updateAllData(period: String)
    fun setCreateOrEditTitle(isEdit: Boolean)
    fun onDeleteItem(param: Int?)
}