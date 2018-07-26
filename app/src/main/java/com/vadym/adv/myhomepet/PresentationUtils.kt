package com.vadym.adv.myhomepet

import android.app.Activity
import android.content.Context
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

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