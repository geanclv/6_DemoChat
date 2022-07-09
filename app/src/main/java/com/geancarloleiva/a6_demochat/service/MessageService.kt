package com.geancarloleiva.a6_demochat.service

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.geancarloleiva.a6_demochat.controller.App
import com.geancarloleiva.a6_demochat.model.Channel
import com.geancarloleiva.a6_demochat.model.Message
import com.geancarloleiva.a6_demochat.util.CHANNEL_GET_ALL

object MessageService {

    val lstChannel = ArrayList<Channel>()
    val lstMessage = ArrayList<Message>()

    fun getChannels(complete: (Boolean) -> Unit) {
        val channelRequest = object : JsonArrayRequest(Method.GET, CHANNEL_GET_ALL, null,
            Response.Listener { response ->
                try{
                    var haveData = false;
                    //the response is a matrix: { {name:"",description:"", id:""}, ..., {name:"",description:"", id:""}}
                    for(x in 0 until response.length()){
                        val channel = response.getJSONObject(x)
                        val name = channel.getString("name")
                        val description = channel.getString("description")
                        val id = channel.getString("id")

                        val newChannel = Channel(name, description, id)
                        this.lstChannel.add(newChannel)
                        complete(true)
                        haveData = true
                    }

                    if (!haveData) {
                        complete(false)
                    }
                }catch (e: Exception){
                    Log.e("ERROR", e.localizedMessage)
                    complete(false)
                }
            }, Response.ErrorListener {
                Log.e("ERROR", "Error getting channels")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.sharedPrefs.authToken}")
                return headers
            }
        }

        //Having an unique queue
        App.sharedPrefs.requestQueue.add(channelRequest)
    }
}