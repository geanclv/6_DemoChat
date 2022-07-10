package com.geancarloleiva.a6_demochat.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geancarloleiva.a6_demochat.R
import com.geancarloleiva.a6_demochat.model.Message
import com.geancarloleiva.a6_demochat.service.UserDataService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter(val context: Context, val messages: ArrayList<Message>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iviUserAvatar: ImageView = itemView.findViewById(R.id.iviUserAvatar)
        val lblUsername: TextView = itemView.findViewById(R.id.lblUsername)
        val lblDateTime: TextView = itemView.findViewById(R.id.lblDateTime)
        val lblMessage: TextView = itemView.findViewById(R.id.lblMessage)

        fun bindMessage(context: Context, message: Message) {
            val resourceId = context.resources.getIdentifier(
                message.userAvatarName, "drawable", context.packageName
            )
            iviUserAvatar?.setImageResource(resourceId)
            iviUserAvatar?.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarColor))
            lblUsername?.text = message.username
            lblDateTime?.text = getDateString(message.dateTime)
            lblMessage?.text = message.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindMessage(context, messages[position])
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    fun getDateString(isoString: String): String {
        val isoFormatter = SimpleDateFormat("dd-MM-yyyy'T'HH:mm'Z'", Locale.getDefault())
        isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var convertedDate = Date()
        try {
            convertedDate = isoFormatter.parse(isoString)
        } catch (e: Exception) {
            Log.e("ERROR", e.localizedMessage)
        }

        val outDateString = SimpleDateFormat("E, h:mm a", Locale.getDefault())

        return outDateString.format(convertedDate)
    }
}