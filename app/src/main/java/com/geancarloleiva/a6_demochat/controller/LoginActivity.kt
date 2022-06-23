package com.geancarloleiva.a6_demochat.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.geancarloleiva.a6_demochat.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnSignin : Button = findViewById(R.id.btnSignin)
        btnSignin.setOnClickListener{
            val signinIntent = Intent(this, NewAccountActivity::class.java)
            startActivity(signinIntent)
        }
    }
}