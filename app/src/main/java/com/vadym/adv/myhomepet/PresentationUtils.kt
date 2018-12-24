package com.vadym.adv.myhomepet

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.JsonToken
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.karumi.dexter.PermissionToken
import com.vadym.adv.myhomepet.R.id.tv_day_from
import kotlinx.android.synthetic.main.view_login.*
import kotlinx.android.synthetic.main.view_my_pet_card_edit.*
import java.util.regex.Pattern

fun Activity.hideKeyboard() = currentFocus?.also { (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply { hideSoftInputFromWindow(it.windowToken, 0) } }

fun Boolean.toAndroidVisibility() = if (this) View.VISIBLE else View.GONE

fun EditText.setSimpleTextWatcher(watcher: (String) -> Unit): TextWatcher =
        object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = watcher(s.toString())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }.also { addTextChangedListener(it) }

fun TextInputEditText.setSimpleTextWatcher(watcher: (String) -> Unit): TextWatcher =
        object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = watcher(s.toString())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }.also { addTextChangedListener(it) }

fun EditText.setTextSmartly(text: CharSequence) {
    if (getText().toString() != text) {
        text.length.also {
            val ss = selectionStart.run { if (this <= it) this else it }
            val se = selectionEnd.run { if (this <= it) this else it }
            setText(text)
            setSelection(ss, se)
        }
    }
}

fun EditText.setFocusableWatcher() {
    setOnTouchListener { view, _ ->
        isCursorVisible = true
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        return@setOnTouchListener false
    }
}

fun Context.getActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

fun Context.showDialog2Button(title: String?,
                              param: String?,
                              buttonNeg: String,
                              onClickNeg: (DialogInterface) -> Unit,
                              buttonPos: String,
                              onClickPos: (DialogInterface) -> Unit): AlertDialog {

    val subView = LayoutInflater.from(this).inflate(R.layout.item_edit_data_owner, null)
    val descriptionField = subView.findViewById<EditText>(R.id.owner_data)

    return AlertDialog.Builder(this).run {
        setView(subView)
        setCancelable(false)
        title?.let { setTitle(it) }
        descriptionField.setText(param)
        setNegativeButton(buttonNeg) { dialog, _ -> onClickNeg(dialog) }
        setPositiveButton(buttonPos) { dialog, _ -> onClickPos(dialog) }
        show().apply {
            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.text_primary_dark))
            this.findViewById<TextView>(android.R.id.message)!!.run {
                movementMethod = LinkMovementMethod.getInstance()
                setLinkTextColor(resources.getColor(R.color.link_color))
            }
        }
    }
}


fun Context.showDialogEditDataOwner(title: String?,
                                    param: String?,
                                    onNoClick: (DialogInterface) -> Unit,
                                    onYesClick: (DialogInterface) -> Unit) = showDialog2Button(
        title, param, resources.getString(R.string.cancel), onNoClick, resources.getString(R.string.change), onYesClick
)


fun Context.showDialogEditDataOwnerQ(title: String?,
                                     param: String?,
                                     buttonNeg: String,
                                     onClickNeg: (DialogInterface) -> Unit,
                                     buttonPos: String,
                                     onClickPos: (DialogInterface) -> Unit): AlertDialog {

    val subView = LayoutInflater.from(this).inflate(R.layout.item_edit_data_owner, null)
    val descriptionField = subView.findViewById<EditText>(R.id.owner_data)

    return AlertDialog.Builder(this).run {
        setView(subView)
        create()
        title?.let { setTitle(it) }
        descriptionField.setText(param)
        setNegativeButton(buttonNeg) { dialog, _ -> onClickNeg(dialog) }
        setPositiveButton(buttonPos) { dialog, _ -> onClickPos(dialog) }
        show().apply {
            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.text_primary_dark))
            this.findViewById<TextView>(android.R.id.message)!!.run {
                movementMethod = LinkMovementMethod.getInstance()
                setLinkTextColor(resources.getColor(R.color.link_color))
            }
        }
    }
}

fun Context.setCalendarDateOfPeriod(year: Int, month: Int, day: Int, textView: TextView) : TextView {
    DatePickerDialog(getActivity(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        textView.text = "$dayOfMonth.$monthOfYear.$year"
    }, year, month, day).show()
    return textView
}

fun Context.onDialogCameraPermission(context: Context, token: PermissionToken) {
    android.app.AlertDialog.Builder(context)
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

fun getIndex(spinner: Spinner, myString: String): Int {
    var index = 0

    for (i in 0 until spinner.count) {
        if (spinner.getItemAtPosition(i).equals(myString)) {
            index = i
        }
    }
    return index
}

fun doNothing() {}

private val PATTERN_EMAIL_ADDRESS_VALIDATOR = Pattern.compile("""(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])""")
fun String.isEmailAddressValid(): Boolean = PATTERN_EMAIL_ADDRESS_VALIDATOR.matcher(this.toLowerCase()).matches()