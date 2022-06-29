package com.geancarloleiva.a6_demochat.controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.geancarloleiva.a6_demochat.R
import com.geancarloleiva.a6_demochat.service.AuthService
import com.geancarloleiva.a6_demochat.service.UserDataService
import com.geancarloleiva.a6_demochat.util.Utils
import java.util.*

class NewAccountActivity : AppCompatActivity() {

    var userAvatar = "avatar_1"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)

        //Go to login button
        val lblGoLogin: TextView = findViewById(R.id.lblGoLogin)
        lblGoLogin.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        //Giving avatar to user
        val iviAvatar: ImageView = findViewById(R.id.iviAvatar)
        iviAvatar.setOnClickListener {
            val random = Random()
            val avatarNumber = random.nextInt(10)
            userAvatar = "avatar_$avatarNumber"

            val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
            iviAvatar.setImageResource(resourceId)
        }

        //Giving color background
        val btnBackground: Button = findViewById(R.id.btnBackground)
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
        val btnCreate: Button = findViewById(R.id.btnCreate)
        val txtName: EditText = findViewById(R.id.txtName)
        val txtEmail: EditText = findViewById(R.id.txtEmail)
        val txtPassword: EditText = findViewById(R.id.txtPassword)
        btnCreate.setOnClickListener {
            val name = txtName.text.toString()
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()

            if (name.isNotBlank() && name.isNotEmpty()) {
                if (email.isNotBlank() && email.isNotEmpty()) {
                    if (password.isNotBlank() && password.isNotEmpty()) {
                        AuthService.createUser(this, name, email, password, userAvatar, avatarColor) { complete ->
                            if (complete) {
                                println(UserDataService.id)
                                println(UserDataService.name)
                                println(UserDataService.avatarName)
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
        }
    }
}