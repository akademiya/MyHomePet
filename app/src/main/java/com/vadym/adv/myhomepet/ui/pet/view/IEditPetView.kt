package com.vadym.adv.myhomepet.ui.pet.view

import android.content.Intent
import com.karumi.dexter.PermissionToken

interface IEditPetView {
    fun updateAllData(period: String)
    fun setCreateOrEditTitle(isEdit: Boolean)
    fun onDeleteItem(param: Int?)

    fun onSelectImageInAlbum(intent: Intent)
    fun onTakePhoto(intent: Intent)
    fun showDialogCameraPermission(token: PermissionToken)
    fun onSuccessValid()
    fun onResetError()
    fun showInvalidValue(error: InvalidData)
    fun setButtonSaveEnabled(enable: Boolean)
    fun setPositionContainerVisibility(isVisible: Boolean)

    enum class InvalidData {
        NO_NAME,
        NO_PERIOD,
        NO_BREED,
        NO_AGE,
        CATEGORY_NOT_SELECTED
    }
}