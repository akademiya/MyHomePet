package com.vadym.adv.myhomepet.ui.pet.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.karumi.dexter.PermissionToken
import com.vadym.adv.myhomepet.*
import com.vadym.adv.myhomepet.data.SqliteDatabase
import com.vadym.adv.myhomepet.di.module.GlideApp
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.presenter.EditPetPresenter
import kotlinx.android.synthetic.main.view_my_pet_card_edit.*
import java.io.IOException
import java.util.*


class EditPetView : BaseActivity(), IEditPetView {

    private lateinit var presenter: EditPetPresenter
    private lateinit var database: SqliteDatabase
    private var noFires = false
    private var category = ""
    private var action = ""
    private var actionPosition = 0
    private var period = ""
    private var country = ""

    private val CAMERA = 0
    private val GALLERY = 1


    @SuppressLint("RestrictedApi")
    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_my_pet_card_edit)
        presenter = EditPetPresenter(this, application)
        database = SqliteDatabase.getInstance(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        button_back.setOnClickListener { presenter.onBackToParent() }

        btn_choose_photo.setOnClickListener { presenter.onSelectImageChecked() }
        btn_take_photo.setOnClickListener { presenter.onTakePhotoChecked(this@EditPetView) } // FIXME: applicationContext.startCaptureCameraImageIntent();
        btn_remove_photo.setOnClickListener { updateImageRepresentation("") }

        val spinnerCategoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.pet_category,
                R.layout.spinner_simple_item
        )
        spinnerCategoryAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        spinner_categories.adapter = spinnerCategoryAdapter
        spinner_categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onResetError()
                presenter.onSpinnerCategorySelected(position)
                category = spinnerCategoryAdapter.getItem(position).toString()
            }
        }

        val spinnerActionAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.pet_action,
                R.layout.spinner_simple_item
        )
        spinnerActionAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        spinner_action.adapter = spinnerActionAdapter
        spinner_action.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                actionPosition = position
                presenter.onSpinnerActionSelected(position)
                action = spinnerActionAdapter.getItem(position).toString()
            }
        }

//        TODO: если выбран пункт "отдать на совсем", то поле периода нужно скрыть
//        when (actionPosition) {
//            0 -> {
//                action_period.visibility = View.GONE
//                hint_period.visibility = View.GONE
//            }
//            1 -> {
//                action_period.visibility = View.VISIBLE
//                hint_period.visibility = View.VISIBLE
//            }
//        }
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        iv_day_from.setOnClickListener { setCalendarDateOfPeriod(year, month, day, tv_day_from) }
        iv_day_to.setOnClickListener { setCalendarDateOfPeriod(year, month, day, tv_day_to) }

        action_period.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.updatePeriod(it)
        }
        pet_name.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.updateName(it)
        }
        pet_breed.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.updateBreed(it)
        }
        pet_age.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.updateAge(it)
        }

        rg_vaccine.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.rb_vaccine_no -> presenter.onVaccineChange(false)
                R.id.rb_vaccine_yes -> presenter.onVaccineChange(true)
            }
        }

        button_save.setOnClickListener {
            presenter.updateData()
            presenter.onValidate()
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        presenter.unbindView(this)
        super.onDetachedFromWindow()
    }

    private fun noFires(block: () -> Unit) {
        noFires = true
        block()
        noFires = false
    }

    override fun setCreateOrEditTitle(isEdit: Boolean) {
        toolbar_title.setText(if (isEdit) R.string.edit_item else R.string.create_item)
        button_delete_me.visibility = isEdit.toAndroidVisibility()
    }

    override fun onSuccessValid() {
        database.addPet(PetModel(this.category, this.action, this.period, this.country))
        presenter.onBackToParent()
    }

    override fun onDeleteItem(param: Int?) {
        button_delete_me.setOnClickListener { database.deletePet(param!!) }
    }

    override fun updateAllData(period: String) {
        this.period = period
    }

    override fun showInvalidValue(error: IEditPetView.InvalidData) {
        when (error) {
            IEditPetView.InvalidData.NO_PERIOD -> til_action_period.error = resources.getString(R.string.no_email)
            IEditPetView.InvalidData.NO_NAME -> til_pet_name.error = resources.getString(R.string.no_email)
            IEditPetView.InvalidData.NO_BREED -> til_pet_breed.error = resources.getString(R.string.no_email)
            IEditPetView.InvalidData.NO_AGE -> til_pet_age.error = resources.getString(R.string.no_email)
            IEditPetView.InvalidData.CATEGORY_NOT_SELECTED -> spCategoryError.visibility = View.VISIBLE
        }
    }

    override fun onResetError() {
        til_action_period.isErrorEnabled = false
        til_pet_name.isErrorEnabled = false
        til_pet_breed.isErrorEnabled = false
        til_pet_age.isErrorEnabled = false
        spCategoryError.visibility = View.GONE
    }

    override fun setButtonSaveEnabled(enable: Boolean) {
        button_save.isEnabled = enable
    }

    /** START SET IMAGE */
    private fun updateImageRepresentation(src: String) {
        photo_pet.visibility = View.GONE
            GlideApp.with(baseContext)
                    .load(src)
                    .fallback(resources.getDrawable(R.drawable.ic_nophoto))
                    .error(resources.getDrawable(R.drawable.ic_nophoto))
                    .centerCrop()
                    .into(iv_pet_image)
    }

    override fun showDialogCameraPermission(token: PermissionToken) {
        AlertDialog.Builder(this@EditPetView)
                .setTitle(R.string.message)
                .setMessage(R.string.message)
                .setNegativeButton(android.R.string.cancel,
                        { dialog, _ ->
                            dialog.dismiss()
                            token.cancelPermissionRequest()
                        }
                )
                .setPositiveButton(android.R.string.ok,
                        { dialog, _ ->
                            dialog.dismiss()
                            token.continuePermissionRequest()
                        }
                )
                .setOnDismissListener({ token.cancelPermissionRequest() })
                .show()
    }

    override fun onSelectImageInAlbum(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null)
            startActivityForResult(intent, GALLERY)
    }

    override fun onTakePhoto(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null)
            startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                photo_pet.visibility = View.VISIBLE
                val imgURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imgURI)
                    presenter.onSaveImage(bitmap)
                    iv_pet_image.setImageBitmap(bitmap)
                    photo_pet.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA) {
            photo_pet.visibility = View.VISIBLE
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            iv_pet_image.setImageBitmap(thumbnail)
            photo_pet.setImageBitmap(thumbnail)
            presenter.onSaveImage(thumbnail)
        }
    }


//    TODO: photo remove button
//    private fun onRemovePhotoVisibility(visibility: Boolean) {
//        btn_remove_photo.visibility = visibility.toAndroidVisibility()
//        if (visibility) btn_remove_photo.clearColorFilter()
//        else return
//    }

    companion object {
        const val ID_CARD = "id_card"
    }
}