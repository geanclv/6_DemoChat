package com.geancarloleiva.a6_demochat.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geancarloleiva.a6_demochat.R
import com.geancarloleiva.a6_demochat.adapter.MessageAdapter
import com.geancarloleiva.a6_demochat.controller.App
import com.geancarloleiva.a6_demochat.databinding.FragmentHomeBinding
import com.geancarloleiva.a6_demochat.model.Channel
import com.geancarloleiva.a6_demochat.model.Message
import com.geancarloleiva.a6_demochat.service.MessageService
import com.geancarloleiva.a6_demochat.service.UserDataService
import com.geancarloleiva.a6_demochat.util.BROADCAST_CHANNEL_DATA_CHANGE
import com.geancarloleiva.a6_demochat.util.SOCKET_URL
import com.geancarloleiva.a6_demochat.util.Utils
import io.socket.client.IO
import io.socket.emitter.Emitter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //My code
    private lateinit var contxt: Context
    private lateinit var lblChannelName: TextView
    private lateinit var selectedChannel: Channel
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var lviMessages: RecyclerView
    private val socket = IO.socket(SOCKET_URL)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.lblLogin
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        //Send Message Button
        contxt = root.context
        val txtMessage: EditText = root.findViewById(R.id.txtMessage)
        val btnSendMessage: ImageButton = root.findViewById(R.id.btnSendMessage)
        btnSendMessage.setOnClickListener {
            Utils.showShortToast(root.context, "hola desde ac??")
            if(App.sharedPrefs.isLoggedIn && txtMessage.text.isNotEmpty()
                && selectedChannel != null){
                val userId = UserDataService.id
                val channelId = selectedChannel!!.id

                //Sending message through socket
                socket.emit("newChannel", txtMessage.text.toString(), userId, channelId,
                    UserDataService.name, UserDataService.avatarName, UserDataService.avatarColor)

                txtMessage.text.clear()
            }
        }

        //Listening to new messages
        socket.on("messageCreated", onNewMessage)

        //Receiving the notification from MainActivity: Channel selected changed
        lblChannelName = root.findViewById(R.id.lblChannelName)
        LocalBroadcastManager.getInstance(root.context).registerReceiver(
            onUpdateWithChannel,
            IntentFilter(BROADCAST_CHANNEL_DATA_CHANGE)
        )

        //Initializing adapter
        messageAdapter = MessageAdapter(contxt!!, MessageService.lstMessage)
        lviMessages = root.findViewById(R.id.lviMessages)
        lviMessages.adapter = messageAdapter
        val layoutManager = LinearLayoutManager(contxt)
        lviMessages.layoutManager = layoutManager

        return root
    }

    private val onNewMessage = Emitter.Listener { args ->
        if(App.sharedPrefs.isLoggedIn) {
            //runOnUiThread{
            val msgBody = args[0] as String
            val username = args[2] as String
            val channelId = args[1] as String
            val userAvatarName = args[3] as String
            val userAvatarColor = args[4] as String
            val id = args[5] as String
            val dateTime = args[6] as String

            val newMessage = Message(
                msgBody, username, channelId, userAvatarName,
                userAvatarColor, id, dateTime
            )

            MessageService.lstMessage.add(newMessage)

            messageAdapter.notifyDataSetChanged()
            lviMessages.smoothScrollToPosition(messageAdapter.itemCount - 1)
            //}
        } else {
            Utils.showShortToast(contxt, "You must be logged in")
        }
    }

    private val onUpdateWithChannel = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(MessageService.lstChannel.isNotEmpty()){
                selectedChannel = MessageService.lstChannel[0]

                if(selectedChannel != null) {
                    lblChannelName.text = selectedChannel.name
                    MessageService.getMessageByChannelId(selectedChannel.id) {complete ->
                        if(complete){
                            messageAdapter.notifyDataSetChanged()
                            if(messageAdapter.itemCount > 0){
                                lviMessages.smoothScrollToPosition(messageAdapter.itemCount - 1)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}