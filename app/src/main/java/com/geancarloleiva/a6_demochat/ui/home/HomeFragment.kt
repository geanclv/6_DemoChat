package com.geancarloleiva.a6_demochat.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.geancarloleiva.a6_demochat.R
import com.geancarloleiva.a6_demochat.controller.MainActivity
import com.geancarloleiva.a6_demochat.databinding.FragmentHomeBinding
import com.geancarloleiva.a6_demochat.service.MessageService
import com.geancarloleiva.a6_demochat.util.BROADCAST_CHANNEL_DATA_CHANGE
import com.geancarloleiva.a6_demochat.util.Utils
import io.socket.emitter.Emitter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //My code
    private lateinit var lblChannelName: TextView

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

        val btnSendMessage: ImageButton = root.findViewById(R.id.btnSendMessage)
        btnSendMessage.setOnClickListener {
            Utils.showShortToast(root.context, "hola desde acá")
        }

        //Receiving the notification from MainActivity: Channel selected changed
        lblChannelName = root.findViewById(R.id.lblChannelName)
        LocalBroadcastManager.getInstance(root.context).registerReceiver(
            onUpdateWithChannel,
            IntentFilter(BROADCAST_CHANNEL_DATA_CHANGE)
        )
        return root
    }

    private val onUpdateWithChannel = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(MessageService.lstChannel.isNotEmpty()){
                lblChannelName.text = "${MessageService.lstChannel[0].name}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}