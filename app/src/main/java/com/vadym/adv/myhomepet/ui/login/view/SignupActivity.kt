package com.vadym.adv.myhomepet.ui.login.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.vadym.adv.myhomepet.R
import com.vadym.adv.myhomepet.setSimpleTextWatcher
import com.vadym.adv.myhomepet.ui.login.ISignupActivity
import com.vadym.adv.myhomepet.ui.login.presenter.SignupPresenter
import com.vadym.adv.myhomepet.ui.main.MainActivity
import kotlinx.android.synthetic.main.view_signup.*

class SignupActivity : AppCompatActivity(), ISignupActivity {

    private var auth: FirebaseAuth? = null
    lateinit var presenter: SignupPresenter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.view_signup)
        presenter = SignupPresenter(this, application)

        auth = FirebaseAuth.getInstance()

        input_name.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.onUpdateOwnerName(it)
        }
        input_email.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.onUpdateOwnerEmail(it)
        }
        input_password.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.onUpdateOwnerPassword(it)
        }
        input_rePassword.setSimpleTextWatcher {
            presenter.onResetError()
            presenter.onUpdateOwnerRePassword(it)
        }
        checkbox_gdpr.setOnCheckedChangeListener { _, isChecked ->
            presenter.onResetError()
            presenter.onUpdateCheckboxGdpr(isChecked)
        }

        btn_signup.setOnClickListener {
            presenter.onValidateAndSave()
        }

        link_login.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

        val spinnerCountryAdapter = ArrayAdapter(this, R.layout.spinner_simple_item, spinner_country)
        spinnerCountryAdapter.setDropDownViewResource(R.layout.spinner_drop_down)
        input_owner_city.adapter = spinnerCountryAdapter
        input_owner_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.onUpdateOwnerAddress(spinner_country[position])
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

    override fun showInvalidValue(errorField: ISignupActivity.InvalidValue) {
        when(errorField) {
            ISignupActivity.InvalidValue.NO_NAME -> til_input_name.error = resources.getString(R.string.no_name)
            ISignupActivity.InvalidValue.NO_EMAIL -> til_input_email.error = resources.getString(R.string.no_email)
            ISignupActivity.InvalidValue.INVALID_EMAIL -> til_input_email.error = resources.getString(R.string.email_incorrect)
            ISignupActivity.InvalidValue.EMAIL_ALREADY_EXIST -> til_input_email.error = resources.getString(R.string.email_exist)
            ISignupActivity.InvalidValue.NO_PASSWORD -> til_input_password.error = resources.getString(R.string.no_password)
            ISignupActivity.InvalidValue.PASSWORD_TOO_SHORT -> til_input_password.error = resources.getString(R.string.password_too_short)
            ISignupActivity.InvalidValue.REPASSWORD_WRONG -> til_input_rePassword.error = resources.getString(R.string.repassword_wrong)
            ISignupActivity.InvalidValue.CHECKBOX_DISABLE -> checkbox_gdpr_error.text = resources.getString(R.string.checkbox_gdpr)
            ISignupActivity.InvalidValue.NO_INTERNET -> resources.getString(R.string.no_internet)
        }
    }

    override fun onResetError() {
        til_input_name.error = ""
        til_input_email.error = ""
        til_input_password.error = ""
        til_input_rePassword.error = ""
        checkbox_gdpr_error.text = ""
    }

    override fun onSignupSuccess() {
        val progressDialog = ProgressDialog(this@SignupActivity)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage(resources.getString(R.string.signup_successful))
        progressDialog.show()

        auth?.createUserWithEmailAndPassword(input_email.text.toString(), input_password.text.toString())?.addOnCompleteListener(this@SignupActivity) { task ->
            startActivity(Intent(this@SignupActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun setButtonCreateEnabled(enable: Boolean) {
        btn_signup.isEnabled = enable
    }

    companion object {
        private val TAG = "SignupActivity"
    }
}