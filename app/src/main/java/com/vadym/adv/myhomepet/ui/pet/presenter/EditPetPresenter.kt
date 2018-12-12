package com.vadym.adv.myhomepet.ui.pet.presenter

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.provider.MediaStore
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.vadym.adv.myhomepet.AndroidApplication
import com.vadym.adv.myhomepet.BasePresenter
import com.vadym.adv.myhomepet.FlowActivity
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.view.EditPetView
import com.vadym.adv.myhomepet.ui.pet.view.IEditPetView

class EditPetPresenter(editPetView: EditPetView, application: Application) : BasePresenter<EditPetView>(editPetView) {

    init { (application as AndroidApplication).applicationComponent.inject(this) }

    private var category = ""
    private var action = ""
    private var period = ""
    private var periodFrom = ""
    private var periodTo = ""
    private var name = ""
    private var breed = ""
    private var age: Int = 0
    private var vaccine = false
    private val param: PetModel? = null
    private var categoryPosition = 0
    private var actionPosition = 0
    private var isValidateSuccess = true

    override fun onBindView() {
        view?.setCreateOrEditTitle(param?.id != null)
        view?.onDeleteItem(param?.id)
    }

    override fun onUnbindView() {}

    fun onValidate() {
        if (period.isBlank()) {
            view?.showInvalidValue(IEditPetView.InvalidData.NO_PERIOD)
            view?.setButtonSaveEnabled(false)
            isValidateSuccess = false
        }

        if (name.isBlank()) {
            view?.showInvalidValue(IEditPetView.InvalidData.NO_NAME)
            view?.setButtonSaveEnabled(false)
            isValidateSuccess = false
        }

        if (breed.isBlank()) {
            view?.showInvalidValue(IEditPetView.InvalidData.NO_BREED)
            view?.setButtonSaveEnabled(false)
            isValidateSuccess = false
        }

        if (age == 0) {
            view?.showInvalidValue(IEditPetView.InvalidData.NO_AGE)
            view?.setButtonSaveEnabled(false)
            isValidateSuccess = false
        }

        if (categoryPosition == 0) {
            view?.showInvalidValue(IEditPetView.InvalidData.CATEGORY_NOT_SELECTED)
            view?.setButtonSaveEnabled(false)
            isValidateSuccess = false
        }

        if (isValidateSuccess) view?.onSuccessValid(category, action, period, periodFrom, periodTo, name, breed, age, vaccine)
    }

    fun onResetError() {
        view?.setButtonSaveEnabled(true)
        view?.onResetError()
        isValidateSuccess = true
    }

    fun onSpinnerCategorySelected(position: Int, category: String) {
        this.categoryPosition = position
        this.category = category
    }
    fun onSpinnerActionSelected(position: Int, action: String) {
        this.actionPosition = position
        this.action = action
        when(position) {
            0 -> view?.setPositionContainerVisibility(false)
            1 -> view?.setPositionContainerVisibility(true)
        }
    }
    fun updatePeriod(period: String?) { this.period = period ?: "" }
    fun updateSelectionPeriod(periodFrom: String?, periodTo: String?) {
        this.periodFrom = periodFrom ?: ""
        this.periodTo = periodTo ?: ""
    }
    fun updateName(name: String) { this.name = name }
    fun updateBreed(breed: String) { this.breed = breed }
    fun updateAge(age: Int?) { this.age = age ?: 0 }

    fun onVaccineChange(isChanged: Boolean) {
        vaccine = isChanged
    }

    fun onSelectImageChecked() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png", "image/jpg"))
        view?.onSelectImageInAlbum(intent)
    }

    fun onTakePhotoChecked(activity: Activity) {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                        view?.showDialogCameraPermission(token)
                    }

                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        view?.onTakePhoto(intent)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        view?.onPermissionDenied()
                    }
                })
                .check()
    }

    fun onBackToParent() {
        view?.goTo(FlowActivity.MY_PET)
    }
}