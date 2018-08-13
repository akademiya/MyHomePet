package com.vadym.adv.myhomepet.ui.login

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.vadym.adv.myhomepet.ui.main.MainActivity
import com.vadym.adv.myhomepet.R
import kotlinx.android.synthetic.main.view_login.*

class LoginActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_login)

        btn_login.setOnClickListener { login() }

        link_signup.setOnClickListener {
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
            finish()
        }
    }

    fun login() {
        Log.d(TAG, "Login")

        if (!validate()) {
            onLoginFailed()
            return
        }

        btn_login.isEnabled = false

        val progressDialog = ProgressDialog(this@LoginActivity) // TODO: I can add style
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Login...")
        progressDialog.show()

//        val email = input_email.text.toString()
        val password = input_password.text.toString()

        // TODO: Implement your own authentication logic here.

        android.os.Handler().postDelayed(
                {
                    // On complete call either onLoginSuccess or onLoginFailed
                    onLoginSuccess()
                    // onLoginFailed();
                    progressDialog.dismiss()
                }, 3000)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish()
            }
        }
    }

    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    private fun onLoginSuccess() {
        btn_login.isEnabled = true
//        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun onLoginFailed() {
        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()
        btn_login.isEnabled = true
    }

    private fun validate(): Boolean {
        var valid = true

//        val email = input_email.text.toString()
        val password = input_password.text.toString()

//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            input_email.error = "enter a valid email address"
//            valid = false
//        } else input_email.error = null

        // TODO validate password
//        if (password.isEmpty() || password.length < 4 || password.length > 10) {
//            input_password.error = "between 4 and 10 alphanumeric characters"
//            valid = false
//        } else input_password.error = null

        return valid
    }

    companion object {
        private val TAG = "LoginActivity"
        private val REQUEST_SIGNUP = 0
    }
}