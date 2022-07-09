package com.geancarloleiva.a6_demochat.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.geancarloleiva.a6_demochat.R
import com.geancarloleiva.a6_demochat.databinding.FragmentHomeBinding
import com.geancarloleiva.a6_demochat.util.Utils

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        try {
            val btnSendMessage: ImageButton = root.findViewById(R.id.btnSendMessage)
            btnSendMessage.setOnClickListener {
                Utils.showShortToast(root.context, "hola desde acá")
            }
        }catch (e: Exception){
            Log.e("ERROR:::::::::::::::::::::::::::::::: ", e.localizedMessage)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}