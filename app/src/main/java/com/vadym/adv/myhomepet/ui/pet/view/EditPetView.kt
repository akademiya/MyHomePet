package com.vadym.adv.myhomepet.ui.pet.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.PermissionToken
import com.vadym.adv.myhomepet.*
import com.vadym.adv.myhomepet.di.module.GlideApp
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.presenter.EditPetPresenter
import kotlinx.android.synthetic.main.view_my_pet_card_edit.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditPetView : BaseActivity(), IEditPetView {

    private lateinit var presenter: EditPetPresenter
    private var database = FirestoreUtils.allPetDocRef
    private var noFires = false
    private val CAMERA = 0
    private val GALLERY = 1
    private lateinit var editPetPhoto: ByteArray
    private var petImagePath: String = ""
    private var isPetPhotoChanged = false
    private val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")


    @SuppressLint("RestrictedApi")
    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_my_pet_card_edit)
        presenter = EditPetPresenter(this, application)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val intent = intent
        val pid = intent.getStringExtra("model")

        if (!pid.isNullOrBlank()) {
            bindDataToEditItem(intent)
        }

        setCreateOrEditTitle(!pid.isNullOrBlank())
        button_back.setOnClickListener { presenter.onBackToParent() }
        btn_choose_photo.setOnClickListener { presenter.onSelectImageChecked() }
        btn_take_photo.setOnClickListener { presenter.onTakePhotoChecked(this@EditPetView) } // FIXME: applicationContext.startCaptureCameraImageIntent();
        btn_remove_photo.setOnClickListener { updateImageRepresentation("") }
        btn_remove_photo.visibility = (isPetPhotoChanged).toAndroidVisibility()

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
                presenter.onSpinnerCategorySelected(position, spinnerCategoryAdapter.getItem(position).toString())
            }
        }
        spinner_categories.hideKeyboardOnTapSpinner()

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
                presenter.onSpinnerActionSelected(position, spinnerActionAdapter.getItem(position).toString())
            }
        }
        spinner_action.hideKeyboardOnTapSpinner()

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
            if (noFires) return@setOnCheckedChangeListener
            when(checkedId) {
                R.id.rb_vaccine_no -> presenter.onVaccineChange(false)
                R.id.rb_vaccine_yes -> presenter.onVaccineChange(true)
            }
        }
        description.setSimpleTextWatcher {
            presenter.updateDescription(it)
        }
        furniture.setSimpleTextWatcher {
            presenter.updateInventory(it)
        }

        button_save.setOnClickListener {
            presenter.updateSelectionPeriod(tv_day_from.text.toString(), tv_day_to.text.toString())
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

    private fun bindDataToEditItem(intent: Intent) {
        val category = intent.getStringExtra("petCategory")
        val action = intent.getStringExtra("action")
        val isSelectedPeriod = intent.getBooleanExtra("isSelectedPeriod", false)
        val period = intent.getStringExtra("period")
        val periodFrom = intent.getStringExtra("periodFrom")
        val periodTo = intent.getStringExtra("periodTo")
        val name = intent.getStringExtra("petName")
        val breed = intent.getStringExtra("breed")
        val age = intent.getStringExtra("age")
        val vaccine = intent.getStringExtra("vaccine")
        val textDescription = intent.getStringExtra("description")
        val textInventory = intent.getStringExtra("inventory")
        val photo = intent.getStringExtra("photo")

        spinner_categories.setSelection(getIndex(spinner_categories, category)) // FIXME
        spinner_action.setSelection(getIndex(spinner_action, action)) // FIXME
        presenter.onSpinnerActionSelected(spinner_action.selectedItemPosition, action) // FIXME
        action_period.setText(period)
        tv_day_from.text = periodFrom
        tv_day_to.text = periodTo
        pet_name.setText(name)
        pet_breed.setText(breed)
        pet_age.setText(age)
        description.setText(textDescription)
        furniture.setText(textInventory)
        if (vaccine == "true") { // FIXME
            rb_vaccine_yes.isChecked
        } else
            rb_vaccine_no.isChecked

        if (photo.isNotBlank()) {
            isPetPhotoChanged = true
            GlideApp.with(this)
                    .load(ImageUtils.pathToReference(photo))
                    .centerCrop()
                    .into(iv_pet_image)
        }
    }

    override fun setCreateOrEditTitle(isEdit: Boolean) {
        toolbar_title.setText(if (isEdit) R.string.edit_item else R.string.create_item)
        button_delete_me.visibility = isEdit.toAndroidVisibility()
    }

    override fun onSuccessValid(category: String,
                                action: String,
                                isPeriodSelection: Boolean,
                                period: String,
                                periodFrom: String,
                                periodTo: String,
                                name: String,
                                breed: String,
                                age: Int,
                                vaccine: Boolean,
                                description: String,
                                inventory: String) {

        database.add(PetModel(
                currentDate = sdf.format(Date()),
                pid = FirebaseAuth.getInstance().currentUser?.uid,
                category = category,
                action = action,
                isPeriodSelection = isPeriodSelection,
                period = period,
                periodFrom = periodFrom,
                periodTo = periodTo,
                petName = name,
                breed = breed,
                petAge = age,
                vaccine = vaccine,
                description = description,
                inventory = inventory,
                pet_photo = petImagePath)
        )
        Toast.makeText(this, resources.getString(R.string.message_save_successful), Toast.LENGTH_SHORT).show()

        presenter.onBackToParent()
    }

    override fun onDeleteItem(param: Int?) {
        button_delete_me.setOnClickListener { database.document("").delete() } // TODO delete
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

    override fun onPermissionDenied() {
        Toast.makeText(this, resources.getString(R.string.google_app_id), Toast.LENGTH_SHORT).show()
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

    override fun setPeriodContainerVisibility(isVisible: Boolean) {
        container_period.visibility = isVisible.toAndroidVisibility()
    }

    /** START SET IMAGE */
    private fun updateImageRepresentation(src: String) {
        btn_remove_photo.visibility = View.GONE
            GlideApp.with(baseContext)
                    .load(src)
                    .fallback(resources.getDrawable(R.drawable.ic_nophoto))
                    .error(resources.getDrawable(R.drawable.ic_nophoto))
                    .centerCrop()
                    .into(iv_pet_image)
    }

    override fun showDialogCameraPermission(token: PermissionToken) {
        onDialogCameraPermission(this@EditPetView, token)
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
        if (requestCode == GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val imgURI = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgURI)
                val bytes = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
                editPetPhoto = bytes.toByteArray()
                GlideApp.with(this)
                        .load(editPetPhoto)
                        .centerCrop()
                        .into(iv_pet_image)
                iv_pet_image.setImageBitmap(bitmap)
                isPetPhotoChanged = true
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            val thumbnail = data.extras!!.get("data") as Bitmap
            val bytes = ByteArrayOutputStream()
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
            editPetPhoto = bytes.toByteArray()
            GlideApp.with(this)
                    .load(editPetPhoto)
                    .centerCrop()
                    .into(iv_pet_image)
            iv_pet_image.setImageBitmap(thumbnail)
            isPetPhotoChanged = true
        }
        initPetPhoto()
    }

    private fun initPetPhoto() {
        if (::editPetPhoto.isInitialized) {
            btn_remove_photo.visibility = View.VISIBLE
            ImageUtils.uploadPetPhoto(editPetPhoto) { imagePath ->
                petImagePath = imagePath
            }
        }
    }

}