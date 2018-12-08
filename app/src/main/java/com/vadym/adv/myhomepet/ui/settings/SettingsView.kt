package com.vadym.adv.myhomepet.ui.settings

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.vadym.adv.myhomepet.*
import com.vadym.adv.myhomepet.R.string.action
import com.vadym.adv.myhomepet.di.module.GlideApp
import kotlinx.android.synthetic.main.view_settings.*
import java.io.ByteArrayOutputStream

class SettingsView : BaseActivity(), ISettingsView {

    private lateinit var presenter: SettingsPresenter
    private var noFires = false
    private lateinit var editOwnerPhoto: ByteArray
    private var isOwnerPhotoChanged = false
    private val CAMERA = 0
    private val GALLERY = 1
    private var cityPosition: Int? = null

    companion object {
        val NAME_KEY = "name"
        val PIN_KEY = "pin"
        val EMAIL_KEY = "email"
        val PHONE_KEY = "phone"
        val CITY_KEY = "city"
        val CITY_ID_KEY = "city_id"
    }

    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_settings)
        presenter = SettingsPresenter(this, application)

        FirestoreUtils.currentUserDocRef.addSnapshotListener { documentSnapshot, _ ->
            FirestoreUtils.getCurrentUser { owner ->
                if (documentSnapshot?.exists()!!) {
                    settings_owner_name.text = documentSnapshot.getString(NAME_KEY)
                    settings_owner_password.text = documentSnapshot.getString(PIN_KEY)?.length?.let { "*".repeat(it) }
                    settings_owner_phone.text = documentSnapshot.getString(PHONE_KEY)
                    settings_owner_city.text = documentSnapshot.getString(CITY_KEY)
                    settings_owner_email.text = documentSnapshot.getString(EMAIL_KEY)
//                    cityPosition = documentSnapshot[CITY_ID_KEY].toString().toInt()

                    edit_owner_name.setOnClickListener {
                        presenter.onEditName(settings_owner_name.text.toString())
                    }
                    edit_owner_phone.setOnClickListener {
                        presenter.onEditPhone(settings_owner_phone.text.toString())
                    }
                    edit_owner_city.setOnClickListener {
                        presenter.onEditCity(settings_owner_city.text.toString())
                    }
                    edit_owner_password.setOnClickListener {
                        error_password_change.visibility = View.GONE
                        presenter.onEditPassword(documentSnapshot.getString(PIN_KEY)!!)
                    }
                }

                if (!isOwnerPhotoChanged && owner.photo != null) {
                    GlideApp.with(this)
                            .load(ImageUtils.pathToReference(owner.photo!!))
                            .placeholder(R.drawable.ic_person)
                            .centerCrop()
                            .into(iv_owner_image)
                }
            }
        }

        sw_notification.setOnCheckedChangeListener { _, isChecked ->
            if (!noFires) presenter.updateCheckedNotification(isChecked)
        }

        btn_choose_photo.setOnClickListener { presenter.onSelectImageChecked() }
        button_save.setOnClickListener { presenter.onSaveButtonClick() }
        button_sign_out.setOnClickListener { presenter.onSignOutClick() }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        presenter.unbindView(this)
        super.onDetachedFromWindow()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            editOwnerPhoto = outputStream.toByteArray()

            GlideApp.with(this)
                    .load(editOwnerPhoto)
                    .circleCrop()
                    .into(iv_owner_image)
            isOwnerPhotoChanged = true
        }
    }

    override fun updateNotification(isChecked: Boolean) = noFires {
        sw_notification.isChecked = isChecked
    }

    override fun updateOwnerName(name: String, onConfirm: () -> Unit) {
        val subView = LayoutInflater.from(this).inflate(R.layout.item_edit_data_owner, null)
        val editableName = subView.findViewById<EditText>(R.id.owner_data)
        editableName.setText(name)

        AlertDialog.Builder(this).apply {
            setView(subView)
            create()
            setTitle(R.string.change_owner_name)
            setPositiveButton(R.string.change) { _, _ ->
                settings_owner_name.text = editableName.text
            }
            setNegativeButton(R.string.cancel) { _, _ -> }
            show()
        }
    }

    override fun updateOwnerCity(city: String, onConfirm: () -> Unit) {
        val subView = LayoutInflater.from(this).inflate(R.layout.item_edit_city_owner, null)
        val editableCity = subView.findViewById<Spinner>(R.id.change_city)
        var editedCity = editableCity.toString()
//        cityPosition?.let { editableCity.setPromptId(it) }

        val spinnerCountryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.city_owner,
                R.layout.spinner_simple_item
        )
        spinnerCountryAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        editableCity.adapter = spinnerCountryAdapter
        editableCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cityPosition = position
                editedCity = spinnerCountryAdapter.getItem(position).toString()
            }
        }

        AlertDialog.Builder(this).apply {
            setView(subView)
            create()
            setTitle(R.string.change_owner_city)
            setPositiveButton(R.string.change) { _, _ ->
                settings_owner_city.text = editedCity
            }
            setNegativeButton(R.string.cancel) { _, _ -> }
            show()
        }
    }

    override fun updateOwnerPhone(phone: String, onConfirm: () -> Unit) {
        val subView = LayoutInflater.from(this).inflate(R.layout.item_edit_phone_owner, null)
        val editablePhone = subView.findViewById<EditText>(R.id.owner_data)
        val phoneCode = subView.findViewById<TextView>(R.id.phone_code)
        editablePhone.setText(phone.substring(4))

        AlertDialog.Builder(this).apply {
            setView(subView)
            create()
            setTitle(R.string.change_owner_phone)
            setPositiveButton(R.string.change) { _, _ ->
                settings_owner_phone.text = phoneCode.text.toString() + editablePhone.text
            }
            setNegativeButton(R.string.cancel) { _, _ -> }
            show()
        }
    }

    override fun updateOwnerPassword(ownerOldPassword: String) {
        val subView = LayoutInflater.from(this).inflate(R.layout.item_edit_password_owner, null)
        val oPassword = subView.findViewById<EditText>(R.id.old_password)
        val nPassword = subView.findViewById<EditText>(R.id.new_password)

        AlertDialog.Builder(this).apply {
            setView(subView)
            create()
            setTitle(R.string.change_owner_password)
            setPositiveButton(R.string.change) { _, _ ->
                val oldPassword = oPassword.text.toString()
                val newPassword = nPassword.text.toString()
                presenter.updatePassword(oldPassword, newPassword)
            }
            setNegativeButton(R.string.cancel) { _, _ -> }
            show()
        }
    }

    override fun onCityChanged(city: String) {
        settings_owner_city.text = city
    }

    override fun onChangedPasswordSuccessful(newPassword: String) {
        settings_owner_password.text = newPassword
    }

    override fun onSelectImageInAlbum(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null)
            startActivityForResult(Intent.createChooser(intent, "Select photo"), GALLERY)
    }

    override fun onSaveDataSettings() {
        FirestoreUtils.updateCurrentUser(
                settings_owner_name.text.toString(),
                settings_owner_password.text.toString(),
                settings_owner_phone.text.toString(),
                settings_owner_city.text.toString(),
                cityPosition,
                if (::editOwnerPhoto.isInitialized) {
                    ImageUtils.uploadPhoto(editOwnerPhoto) {}.toString()
                } else null
        )
        Toast.makeText(this, resources.getString(R.string.message_save_successful), Toast.LENGTH_SHORT).show()
    }

    override fun onErrorEditPassword(error: ISettingsView.ErrorEditing) { //error: ISettingsView.ErrorEditing
//        error_password_change.visibility = View.VISIBLE
        when(error) {
            ISettingsView.ErrorEditing.ERROR_PASSWORD -> error_password_change.visibility = View.VISIBLE
            else -> { }
        }
    }

    private fun noFires(block: () -> Unit) {
        noFires = true
        block()
        noFires = false
    }
}