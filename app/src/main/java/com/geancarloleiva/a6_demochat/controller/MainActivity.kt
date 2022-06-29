package com.geancarloleiva.a6_demochat.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.geancarloleiva.a6_demochat.R
import com.geancarloleiva.a6_demochat.databinding.ActivityMainBinding
import com.geancarloleiva.a6_demochat.service.AuthService
import com.geancarloleiva.a6_demochat.service.UserDataService
import com.geancarloleiva.a6_demochat.util.BROADCAST_USER_DATA_CHANGE

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //My code
    private val iviAvatar: ImageView = findViewById(R.id.iviAvatar)
    private val txtName: TextView = findViewById(R.id.txtName)
    private val txtEmail: TextView = findViewById(R.id.txtEmail)
    private val btnLogin: Button = findViewById(R.id.btnLogin)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //My code
        //Go to login
        btnLogin.setOnClickListener {
            if (AuthService.isLoggedIn) {
                UserDataService.logout()
                btnLogin.text = R.string.btn_login.toString()
                txtName.text = R.string.nav_header_title.toString()
                txtEmail.text = R.string.nav_header_subtitle.toString()
                iviAvatar.setImageResource(R.mipmap.ic_launcher_round)
                iviAvatar.setBackgroundColor(Color.TRANSPARENT)
            } else {
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            }
        }

        //Add channel
        val btnAddChannel: Button = findViewById(R.id.btnAddChannel)
        btnAddChannel.setOnClickListener {

        }

        //Getting info from Broadcast (after user loggin)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE)
        )
    }

    //complementing the info received from the Broadcast
    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isLoggedIn) {
                txtName.text = UserDataService.name
                txtEmail.text = UserDataService.email
                val resourceId = resources.getIdentifier(
                    UserDataService.avatarName,
                    "drawable", packageName
                )
                iviAvatar.setImageResource(resourceId)
                iviAvatar.setBackgroundColor(
                    UserDataService.returnAvatarColor(UserDataService.avatarColor)
                )

                btnLogin.text = "Logout"
            }
        }
    }

    //Not my code
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}