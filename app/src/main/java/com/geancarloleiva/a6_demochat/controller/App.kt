package com.geancarloleiva.a6_demochat.controller

import android.app.Application
import com.geancarloleiva.a6_demochat.util.SharedPrefs

class App: Application() {

    companion object{
        lateinit var sharedPrefs: SharedPrefs
    }

    override fun onCreate() {
        sharedPrefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}