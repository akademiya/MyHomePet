package com.vadym.adv.myhomepet.ui.pet.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.vadym.adv.myhomepet.*
import com.vadym.adv.myhomepet.R.string.action
import com.vadym.adv.myhomepet.data.SqliteDatabase
import com.vadym.adv.myhomepet.ui.pet.PetModel
import com.vadym.adv.myhomepet.ui.pet.presenter.EditPetPresenter
import kotlinx.android.synthetic.main.view_my_pet_card_edit.*

class EditPetView : BaseActivity(), IEditPetView {

    private lateinit var presenter: EditPetPresenter
    private lateinit var database: SqliteDatabase
    private var category = ""
    private var action = ""
    private var period = ""
    private var country = ""

    private val spinner_category = arrayOf(
            "категория питомца",
            "собака", "кошка", "морская свинка",
            "декоративная мишь", "хомяк", "белка", "шиншила",
            "попугай", "сова", "канарейка", "курица",
            "паук", "декоративный таракан", "бабочка"
    )

    private val spinner_acton = arrayOf("главное действие", "отдам в хорошие руки", "отдам на время")

    private val spinner_country = arrayOf(
            "Киев",
            "Винницкая область", "Винница", "Жмеринка",
            "Волынская область", "Луцк",
            "Днепропетровская область", "Днепродзержинск", "Днепропетровск", "Кривой Рог", "Никополь", "Павлоград",
            "Донецкая область", "Артемовск", "Горловка", "Дзержинск", "Донецк", "Енакиево", "Константиновка", "Краматорск", "Макеевка", "Мариуполь", "Славянск", "Торез", "Харцызск",
            "Житомирская область", "Бердичев", "Житомир",
            "Закарпатская область", "Мукачево", "Ужгород",
            "Запорожская область", "Бердянск", "Запорожье", "Мукачево",
            "Ивано-Франковская область", "Ивано-Франковск",
            "Киевская область", "Белая Церковь", "Борисполь", "Бровары", "Ирпень",
            "Кировоградская область", "Александрия", "Кировоград",
            "Луганская область", "Алчевск", "Красный Луч", "Лисичанск", "Луганск", "Свердловск", "Северодонецк", "Стаханов",
            "Львовская область", "Львов", "Жовква",
            "Николаевская область", "Николаев",
            "Одесская область", "Измаил", "Ильичевск", "Одесса",
            "Полтавская область", "Комсомольск", "Кременчуг", "Полтава",
            "Ровенская область", "Дубно", "Ровно",
            "Сумская область", "Конотоп", "Сумы", "Шостка",
            "Тернопольская область", "Тернополь",
            "Харьковская область", "Лозовая", "Харьков",
            "Херсонская область", "Каховка", "Херсон",
            "Хмельницкая область", "Каменец-Подольский", "Хмельницкий",
            "Черкасская область", "Смела", "Умань", "Черкассы",
            "Черниговская область", "Нежин", "Чернигов", "Черновцы"
    )


//    @SuppressLint("RestrictedApi")
//    private val noPhotoDrawable = AppCompatDrawableManager.get().getDrawable(baseContext, R.drawable.ic_nophoto).apply { setColorFilter(baseContext.resources.getColor(R.color.ic_normal_dark), PorterDuff.Mode.MULTIPLY) }


    @SuppressLint("RestrictedApi")
    override fun init(savedInstanceState: Bundle?) {
        super.setContentView(R.layout.view_my_pet_card_edit)
        presenter = EditPetPresenter(this, application)
        database = SqliteDatabase.getInstance(this)

        btn_choose_photo.setOnClickListener { applicationContext.startTakeGalleryImageIntent() }
        btn_take_photo.setOnClickListener { applicationContext.startCaptureCameraImageIntent() }
        btn_remove_photo.setOnClickListener { updateImageRepresentation("") }

        val name = "rr" //pet_name.text.toString()
//        val photo = 0 //resources.getDrawable(R.drawable.kisa2)

        action_period.setSimpleTextWatcher { presenter.updatePeriod(it) }

        button_save.setOnClickListener {
            presenter.updateData()
            database.addPet(PetModel(this.category, this.action, this.period, this.country))
            presenter.onBackToParent()
        }

        val spinnerCategoryAdapter = ArrayAdapter(this, R.layout.spinner_simple_item, spinner_category)
        spinnerCategoryAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        spinner_categories.adapter = spinnerCategoryAdapter
        spinner_categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = spinner_category[position]
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

        val spinnerCountryAdapter = ArrayAdapter(this, R.layout.spinner_simple_item, spinner_country)
        spinnerCountryAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        owner_country.adapter = spinnerCountryAdapter
        owner_country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                country = spinner_country[position]
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
//            GlideApp.with(baseContext)
//                    .load(src)
//                    .fallback(noPhotoDrawable)
//                    .error(noPhotoDrawable)
//                    .centerCrop()
//                    .into(iv_pet_image)
        }
    }
}