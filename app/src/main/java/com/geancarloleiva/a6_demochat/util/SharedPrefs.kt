package com.geancarloleiva.a6_demochat.util

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.toolbox.Volley

class SharedPrefs(context: Context) {

    val PREF_FILENAME = "PREFERENCES"
    val prefs: SharedPreferences = context.getSharedPreferences(PREF_FILENAME, 0)

    val IS_LOGGED_IN = "IS_LOGGED_IN"
    val AUTH_TOKEN = "AUTH_TOKEN"
    val USER_EMAIL = "USER_EMAIL"

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var authToken: String?
        get() = prefs.getString(AUTH_TOKEN, "")
        set(value) = prefs.edit().putString(AUTH_TOKEN, value).apply()

    var userEmail: String?
        get() = prefs.getString(USER_EMAIL, "")
        set(value) = prefs.edit().putString(USER_EMAIL, value).apply()

    val requestQueue = Volley.newRequestQueue(context)
}