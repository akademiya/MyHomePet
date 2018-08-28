package com.vadym.adv.myhomepet.ui.pet.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.vadym.adv.myhomepet.*
import com.vadym.adv.myhomepet.data.SqliteDatabase
import com.vadym.adv.myhomepet.di.module.GlideApp
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.presenter.EditPetPresenter
import kotlinx.android.synthetic.main.view_my_pet_card_edit.*


class EditPetView : BaseActivity(), IEditPetView {

    private lateinit var presenter: EditPetPresenter
    private lateinit var database: SqliteDatabase
    private lateinit var petModel: PetModel
//    private val idCard = intent.getIntExtra(ID_CARD, 0)
    private var category = ""
    private var action = ""
    private var period = ""
    private var country = ""

//    lateinit var formatHelper: FormatHelper
//
//    private val spinnerMainCategoryAdapter = SpinnerAdapter<PetActionSpinner> { isDropDown, position, convertView, parent ->
//        val convertView = convertView ?: if (isDropDown) {
//            LayoutInflater.from(baseContext).inflate(R.layout.spinner_simple_item, parent, false)
//        } else {
//            LayoutInflater.from(baseContext).inflate(R.layout.spinner_drop_down, parent, false)
//        }
//
//        convertView.value.apply {
//            val mainAction = items[position]
//            text = formatHelper.formatPetAction(mainAction)
//
////            if (isDropDown) {
////                val backgroundColor = if (isItemSelected(diningOption)) filterSelectedBackground else filterNormalBackground
////                convertView.setBackgroundColor(backgroundColor)
////            }
//        }
//
//        convertView
//    }

    private val spinner_category = arrayOf(
            "категория питомца",
            "собака", "кошка", "морская свинка",
            "декоративная мишь", "хомяк", "белка", "шиншила",
            "попугай", "сова", "канарейка", "курица",
            "паук", "декоративный таракан", "бабочка"
    )

    private val spinner_acton = arrayOf("отдам в хорошие руки", "отдам на время")

    @SuppressLint("RestrictedApi")
    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_my_pet_card_edit)
        presenter = EditPetPresenter(this, application)
        database = SqliteDatabase.getInstance(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        button_back.setOnClickListener { presenter.onBackToParent() }


        btn_choose_photo.setOnClickListener { applicationContext.startTakeGalleryImageIntent() }
        btn_take_photo.setOnClickListener { applicationContext.startCaptureCameraImageIntent() }
        btn_remove_photo.setOnClickListener { updateImageRepresentation("") }


        action_period.setSimpleTextWatcher { presenter.updatePeriod(it) }

        button_save.setOnClickListener {
            presenter.updateData()
            database.addPet(PetModel(this.category, this.action, this.period, this.country))
            presenter.onBackToParent()
        }


        /**
         * Задизейблить первую позицию списка - это и будет hint
         */
//        val spinnerCategoryAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_simple_item, spinner_category) {
//            override fun isEnabled(position: Int): Boolean {
//                return if (position == 0) false else true
//            }
//
//            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//                val view = super.getDropDownView(position, convertView, parent)
//                val tv = view as TextView
//                if (position == 0) {
//                    tv.setTextColor(Color.GREEN)
//                }
//                return view
//            }
//        }

        val spinnerCategoryAdapter = ArrayAdapter(this, R.layout.spinner_simple_item, spinner_category)
        spinnerCategoryAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        spinner_categories.adapter = spinnerCategoryAdapter
//        spinner_categories.adapter = spinnerMainActionAdapter
        spinner_categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedItem = spinnerMainActionAdapter.getItem(position)
//                spinnerMainActionAdapter.selectedItem = selectedItem
                category = spinner_category[position] // if (position > 0)
            }
        }

        val spinnerActionAdapter = ArrayAdapter(this, R.layout.spinner_simple_item, spinner_acton)
        spinnerActionAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        spinner_action.adapter = spinnerActionAdapter
        spinner_action.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                action = spinner_acton[position]
            }
        }

        rg_vaccine.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.rb_vaccine_no -> presenter.onVaccineChange(false)
                R.id.rb_vaccine_yes -> presenter.onVaccineChange(true)
            }
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.bindView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unbindView(this)
    }

    override fun setCreateOrEditTitle(isEdit: Boolean) {
        toolbar_title.setText(if (isEdit) R.string.edit_item else R.string.create_item)
        button_delete_me.visibility = isEdit.toAndroidVisibility()
    }

    override fun onDeleteItem(param: Int?) {
        button_delete_me.setOnClickListener { database.deletePet(param!!) }
    }

    override fun updateAllData(period: String) {
        this.period = period
    }

    private fun updateImageRepresentation(src: String) {
        if (src.isBlank()) {
            iv_pet_image.setImageResource(R.drawable.ic_nophoto)
            iv_pet_image.setColorFilter(resources.getColor(R.color.ic_normal_dark))
            btn_remove_photo.visibility = View.GONE
        } else {
            iv_pet_image.clearColorFilter()
            btn_remove_photo.visibility = View.VISIBLE
            GlideApp.with(baseContext)
                    .load(src)
                    .fallback(resources.getDrawable(R.drawable.ic_nophoto))
                    .error(resources.getDrawable(R.drawable.ic_nophoto))
                    .centerCrop()
                    .into(iv_pet_image)
        }
    }

    companion object {
        const val ID_CARD = "id_card"
    }
}