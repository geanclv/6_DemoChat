package com.geancarloleiva.a6_demochat.controller

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.widget.*
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
import com.geancarloleiva.a6_demochat.model.Channel
import com.geancarloleiva.a6_demochat.service.AuthService
import com.geancarloleiva.a6_demochat.service.MessageService
import com.geancarloleiva.a6_demochat.service.UserDataService
import com.geancarloleiva.a6_demochat.util.BROADCAST_USER_DATA_CHANGE
import com.geancarloleiva.a6_demochat.util.SOCKET_URL
import com.geancarloleiva.a6_demochat.util.Utils
import io.socket.client.IO
import io.socket.emitter.Emitter

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //My code
    private lateinit var iviAvatar: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var btnLogin: Button
    private lateinit var channelAdapter: ArrayAdapter<Channel>

    private val socket = IO.socket(SOCKET_URL)

    private fun setupAdapter(){
        channelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
            MessageService.lstChannel)
        val lstChanelList: ListView = findViewById(R.id.lstChanelList)
        lstChanelList.adapter = channelAdapter
    }

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
        Utils.hideKeyboard(this, this)
        iviAvatar = findViewById(R.id.iviAvatar)
        txtName = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtEmail)
        btnLogin = findViewById(R.id.btnLogin)

        //Go to login
        btnLogin.setOnClickListener {
            if (AuthService.isLoggedIn) {
                UserDataService.logout()
                btnLogin.text = getString(R.string.btn_login)
                txtName.text = getString(R.string.nav_header_title)
                txtEmail.text = getString(R.string.nav_header_subtitle)
                iviAvatar.setImageResource(R.mipmap.ic_launcher_round)
                iviAvatar.setBackgroundColor(Color.TRANSPARENT)

                MessageService.getChannels(this) { complete ->
                    if(complete){
                        channelAdapter.notifyDataSetChanged()
                    }
                }
            } else {
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            }
        }

        //Add channel
        val btnAddChannel: Button = findViewById(R.id.btnAddChannel)
        btnAddChannel.setOnClickListener {
            if(AuthService.isLoggedIn){
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.dialog_add_channel, null)

                builder.setView(dialogView)
                    .setPositiveButton("Add") { dialogInterface, i ->
                        val channelName = dialogView.findViewById<EditText>(R.id.txtChannelName)
                            .text.toString()
                        val channelDescription = dialogView.findViewById<EditText>(R.id.txtChannelDescription)
                            .text.toString()

                        //Using sockets to transfer info (API must support it)
                        socket.emit("newChannel", channelName, channelDescription)
                    }
                    .setNegativeButton("Cancel") { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }
                    .show()

                Utils.hideKeyboard(this, this)
            } else {
                Utils.showShortToast(this,  "Please log in")
            }
        }

        socket.connect()

        //When an event is received from API
        socket.on("channelCreated", onNewChannel)
    }

    override fun onResume() {
        //Getting info from Broadcast (after user loggin)
        LocalBroadcastManager.getInstance(this).registerReceiver(
            userDataChangeReceiver,
            IntentFilter(BROADCAST_USER_DATA_CHANGE)
        )

        super.onResume()
    }

    //Creating a listener for  channels
    private val onNewChannel = Emitter.Listener { args ->
        runOnUiThread{
            val channelName = args[0] as String
            val channelDescription = args[1] as String
            val channelId = args[2] as String

            val newChannel = Channel(channelName, channelDescription, channelId)
            MessageService.lstChannel.add(newChannel)

            channelAdapter.notifyDataSetChanged()

        }
    }

    override fun onDestroy() {
        socket.disconnect()

        //Unregistering the Broadcast
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)

        super.onDestroy()
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