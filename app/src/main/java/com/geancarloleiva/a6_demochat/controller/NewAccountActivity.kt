package com.geancarloleiva.a6_demochat.controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.geancarloleiva.a6_demochat.R
import java.util.*

class NewAccountActivity : AppCompatActivity() {

    var defaultAvatar = "avatar_1"
    var defaultBackground = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)

        val lblGoLogin: TextView = findViewById(R.id.lblGoLogin)
        lblGoLogin.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        val iviAvatar: ImageView = findViewById(R.id.iviAvatar)
        iviAvatar.setOnClickListener {
            val random = Random()
            val avatarNumber = random.nextInt(10)
            val userAvatar = "avatar_$avatarNumber"

            val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
            iviAvatar.setImageResource(resourceId)
        }

        val btnBackground: Button = findViewById(R.id.btnBackground)
        btnBackground.setOnClickListener{
            val random = Random()
            val r = random.nextInt(255)
            val g = random.nextInt(255)
            val b = random.nextInt(255)

            iviAvatar.setBackgroundColor(Color.rgb(r, g, b))

            //To send to DB
            val savedR = r.toDouble() / 255
            val savedG = g.toDouble() / 255
            val savedB = b.toDouble() / 255

            defaultBackground = "[$savedR, $savedG, $savedB, 1]"
        }
    }
}