package com.vadym.adv.myhomepet.ui.pet.presenter

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
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
import java.io.ByteArrayOutputStream

class EditPetPresenter(editPetView: EditPetView, application: Application) : BasePresenter<EditPetView>(editPetView) {

    init { (application as AndroidApplication).applicationComponent.inject(this) }

    private var period = ""
    private var name = ""
    private var breed = ""
    private var age = ""
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

        if (age.isBlank()) {
            view?.showInvalidValue(IEditPetView.InvalidData.NO_AGE)
            view?.setButtonSaveEnabled(false)
            isValidateSuccess = false
        }

        if (categoryPosition == 0) {
            view?.showInvalidValue(IEditPetView.InvalidData.CATEGORY_NOT_SELECTED)
            view?.setButtonSaveEnabled(false)
            isValidateSuccess = false
        }

        if (isValidateSuccess) view?.onSuccessValid()
    }

    fun onResetError() {
        view?.setButtonSaveEnabled(true)
        view?.onResetError()
        isValidateSuccess = true
    }

    fun onSpinnerCategorySelected(position: Int) { this.categoryPosition = position }
    fun onSpinnerActionSelected(position: Int) { this.actionPosition = position }
    fun updatePeriod(period: String) { this.period = period }
    fun updateName(name: String) { this.name = name }
    fun updateBreed(breed: String) { this.breed = breed }
    fun updateAge(age: String) { this.age = age }

    fun updateData() {
        view?.updateAllData(period)
    }

    fun onVaccineChange(isChange: Boolean) {
        param?.vaccine = isChange
    }

    fun onSelectImageChecked() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
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
//                        Snackbar.make(mainContainer!!, R.string.google_app_id, Snackbar.LENGTH_LONG).show() FIXME: make toast in view
                    }
                })
                .check()
    }

    fun onSaveImage(bitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

//        val wallpaperDirectory = File((Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
//        try
//        {
//            val f = File(wallpaperDirectory, ((Calendar.getInstance().timeInMillis).toString() + ".jpg"))
//            f.createNewFile()
//            val fo = FileOutputStream(f)
//            fo.write(bytes.toByteArray())
//            MediaScannerConnection.scanFile(this,
//                    arrayOf(f.path),
//                    arrayOf("image/jpeg"), null)
//            fo.close()
//
////            return f.getAbsolutePath()
//        }
//        catch (e1: IOException) {
//            e1.printStackTrace()
//        }
//        return ""
    }

    fun onBackToParent() {
        view?.goTo(FlowActivity.MY_PET)
    }
}