package com.geancarloleiva.a6_demochat.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.geancarloleiva.a6_demochat.R
import com.geancarloleiva.a6_demochat.service.AuthService
import com.geancarloleiva.a6_demochat.util.Utils

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnSignin : Button = findViewById(R.id.btnSignin)
        btnSignin.setOnClickListener{
            val signinIntent = Intent(this, NewAccountActivity::class.java)
            startActivity(signinIntent)
        }

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val txtUser: EditText = findViewById(R.id.txtUser)
        val txtPassword: EditText = findViewById(R.id.txtPassword)
        btnLogin.setOnClickListener{
            val user = txtUser.text.toString()
            val password = txtPassword.text.toString()

            if(user.isNotEmpty() && user.isNotBlank()){
                if(password.isNotEmpty() && password.isNotBlank()){
                    AuthService.loginUser(this, user, password){complete ->
                        if(complete){
                            println(AuthService.authToken)
                            println(AuthService.userEmail)
                            Utils.showShortToast(this, "Login OK")
                        }
                    }
                } else{
                    Utils.showShortToast(this, "Password is required")
                }
            } else {
                Utils.showShortToast(this, "User/Email is required")
            }
        }
    }
}