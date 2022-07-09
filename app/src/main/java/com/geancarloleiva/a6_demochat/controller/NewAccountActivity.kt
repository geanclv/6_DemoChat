package com.geancarloleiva.a6_demochat.controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.geancarloleiva.a6_demochat.R
import com.geancarloleiva.a6_demochat.service.AuthService
import com.geancarloleiva.a6_demochat.service.UserDataService
import com.geancarloleiva.a6_demochat.util.Utils
import java.util.*

class NewAccountActivity : AppCompatActivity() {

    private var userAvatar = "avatar_1"
    private var avatarColor = "[0.5, 0.5, 0.5, 1]"
    private lateinit var progressBarCreate: ProgressBar
    private lateinit var iviAvatar: ImageView
    private lateinit var btnBackground: Button
    private lateinit var btnCreate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)

        progressBarCreate = findViewById(R.id.progressBarCreate)
        iviAvatar = findViewById(R.id.iviAvatar)
        btnBackground = findViewById(R.id.btnBackground)
        btnCreate = findViewById(R.id.btnCreate)

        //Hidden the progress bar
        progressBarCreate.visibility = View.INVISIBLE

        //Go to login button
        val lblGoLogin: TextView = findViewById(R.id.lblGoLogin)
        lblGoLogin.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        //Giving avatar to user
        iviAvatar.setOnClickListener {
            val random = Random()
            val avatarNumber = random.nextInt(9) + 1
            userAvatar = "avatar_$avatarNumber"

            val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
            iviAvatar.setImageResource(resourceId)
        }

        //Giving color background
        btnBackground.setOnClickListener {
            val random = Random()
            val r = random.nextInt(255)
            val g = random.nextInt(255)
            val b = random.nextInt(255)

            iviAvatar.setBackgroundColor(Color.rgb(r, g, b))

            //To send to DB
            val savedR = r.toDouble() / 255
            val savedG = g.toDouble() / 255
            val savedB = b.toDouble() / 255

            avatarColor = "[$savedR, $savedG, $savedB, 1]"
        }

        //Creating user
        btnCreate.setOnClickListener {
            createUser()
        }
    }

    private fun createUser(){
        progressBarVisible(true)
        val name = findViewById<EditText?>(R.id.txtName).text.toString()
        val email = findViewById<EditText?>(R.id.txtEmail).text.toString()
        val password = findViewById<EditText?>(R.id.txtPassword).text.toString()

        if (name.isNotBlank() && name.isNotEmpty()) {
            if (email.isNotBlank() && email.isNotEmpty()) {
                if (password.isNotBlank() && password.isNotEmpty()) {
                    AuthService.createUser(this, name, email, password, userAvatar, avatarColor) { complete ->
                        if (complete) {
                            Utils.showShortToast(this, "OK")
                            val loginIntent = Intent(this, LoginActivity::class.java)
                            startActivity(loginIntent)
                        } else {
                            Utils.showShortToast(this, "ERROR")
                        }
                    }
                } else {
                    Utils.showShortToast(this, "Password is required")
                }
            } else {
                Utils.showShortToast(this, "Email is required")
            }
        } else {
            Utils.showShortToast(this, "Name is required")
        }
        progressBarVisible(false)
    }

    private fun progressBarVisible(enable: Boolean){
        if(enable) {
            progressBarCreate.visibility = View.VISIBLE
        } else {
            progressBarCreate.visibility = View.INVISIBLE
        }
        iviAvatar.isEnabled = !enable
        btnBackground.isEnabled = !enable
        btnCreate.isEnabled = !enable
    }
}