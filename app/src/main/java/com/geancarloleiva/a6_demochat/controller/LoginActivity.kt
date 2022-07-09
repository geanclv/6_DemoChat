package com.geancarloleiva.a6_demochat.controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.geancarloleiva.a6_demochat.R
import com.geancarloleiva.a6_demochat.service.AuthService
import com.geancarloleiva.a6_demochat.util.BROADCAST_USER_DATA_CHANGE
import com.geancarloleiva.a6_demochat.util.Utils

class LoginActivity : AppCompatActivity() {

    private lateinit var btnSignin : Button
    private lateinit var btnLogin: Button
    private lateinit var progressBarLogin: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBarLogin = findViewById(R.id.progressBarLogin)

        //Go to create account
        btnSignin = findViewById(R.id.btnSignin)
        btnSignin.setOnClickListener{
            val signinIntent = Intent(this, NewAccountActivity::class.java)
            startActivity(signinIntent)
            finish() //avoids to stack activities while navigating in the app
        }

        //Login
        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener{
            login()
        }

        progressBarVisible(false)
    }

    private fun login(){
        progressBarVisible(true)
        Utils.hideKeyboard(this, this)
        val user = findViewById<EditText>(R.id.txtUser).text.toString()
        val password = findViewById<EditText>(R.id.txtPassword).text.toString()

        if(user.isNotEmpty() && user.isNotBlank()){
            if(password.isNotEmpty() && password.isNotBlank()){
                AuthService.loginUser(user, password){complete ->
                    if(complete){
                        Utils.showShortToast(this, "Login OK")
                        //Broadcasting that the user is Logged in
                        val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                        finish()
                    } else {
                        Utils.showShortToast(this, "Invalid user or password")
                    }
                }
            } else{
                Utils.showShortToast(this, "Password is required")
            }
        } else {
            Utils.showShortToast(this, "User/Email is required")
        }
        progressBarVisible(false)
    }

    private fun progressBarVisible(enable: Boolean){
        if(enable) {
            progressBarLogin.visibility = View.VISIBLE
        } else {
            progressBarLogin.visibility = View.INVISIBLE
        }
        btnSignin.isEnabled = !enable
        btnLogin.isEnabled = !enable
    }
}