package com.vadym.adv.myhomepet.ui.pet.view

import android.content.Intent
import com.karumi.dexter.PermissionToken

interface IEditPetView {
//    fun updateAllData(period: String)
    fun onDeleteItem(param: Int?)
    fun onSelectImageInAlbum(intent: Intent)
    fun onTakePhoto(intent: Intent)
    fun onResetError()
    fun onPermissionDenied()

    fun showDialogCameraPermission(token: PermissionToken)
    fun showInvalidValue(error: InvalidData)
    fun setCreateOrEditTitle(isEdit: Boolean)
    fun setButtonSaveEnabled(enable: Boolean)
    fun setPositionContainerVisibility(isVisible: Boolean)
    fun onSuccessValid(category: String,
                       action: String,
                       period: String,
                       periodFrom: String,
                       periodTo: String,
                       name: String,
                       breed: String,
                       age: Int,
                       vaccine: Boolean,
                       description: String,
                       inventory: String)

    enum class InvalidData {
        NO_NAME,
        NO_PERIOD,
        NO_BREED,
        NO_AGE,
        CATEGORY_NOT_SELECTED
    }
}