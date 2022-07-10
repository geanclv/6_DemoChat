package com.geancarloleiva.a6_demochat.service

import android.graphics.Color
import com.geancarloleiva.a6_demochat.controller.App
import java.util.*

object UserDataService {

    var id = ""
    var name = ""
    var email = ""
    var avatarName = ""
    var avatarColor = ""

    fun returnAvatarColor(components: String): Int{
        val strippedColor = components
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)
        if(scanner.hasNext()){
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }

        return Color.rgb(r, g, b)
    }

    fun logout(){
        id = ""
        name = ""
        email = ""
        avatarName = ""
        avatarColor = ""
        App.sharedPrefs.authToken = ""
        App.sharedPrefs.userEmail = ""
        App.sharedPrefs.isLoggedIn = false
        MessageService.clearChannels()
        MessageService.clearMessages()
    }
}