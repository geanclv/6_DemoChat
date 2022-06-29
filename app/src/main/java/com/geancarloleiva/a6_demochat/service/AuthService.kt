package com.geancarloleiva.a6_demochat.service

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.geancarloleiva.a6_demochat.util.USER_CREATE
import com.geancarloleiva.a6_demochat.util.USER_LOGIN
import org.json.JSONObject

object AuthService {

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

    //HTTP functions that involve Authorization
    fun exampleResponseString(
        //this fun is only as example when you have a String Response. No valid for this app/operation
        context: Context, name: String, complete: (Boolean) -> Unit
    ) {
        val url = USER_CREATE

        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        val requestBody = jsonBody.toString()

        val request = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String> { response ->
                complete(true)
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not connect to service: $error")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    fun createUser(
        context: Context, name: String, email: String, password: String,
        avatarName: String, avatarColor: String, complete: (Boolean) -> Unit
    ) {
        val url = USER_CREATE

        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()

        val createRequest = object : JsonObjectRequest(Request.Method.POST, url, null,
            Response.Listener { response ->
                try {
                    UserDataService.id = response.getString("id")
                    UserDataService.name = response.getString("name")
                    UserDataService.email = response.getString("email")
                    UserDataService.avatarName = response.getString("avatarName")
                    UserDataService.avatarColor = response.getString("avatarColor")
                    complete(true)
                } catch (e: Exception){
                    Log.d("ERROR", "No creation: ${e.localizedMessage}")
                    complete(false)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not connect to service: $error")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            //if the API's header need aditional info (like a token), we should modify the headers
            //only as example, not used now
            /*override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }*/
        }

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, USER_LOGIN, null,
            Response.Listener { response ->
                try {
                    authToken = response.getString("token")
                    userEmail = response.getString("email")
                    isLoggedIn = true
                    complete(true)
                } catch (e: Exception) {
                    Log.d("ERROR", e.localizedMessage)
                    complete(false)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not connect to service: $error")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(loginRequest)
    }
}