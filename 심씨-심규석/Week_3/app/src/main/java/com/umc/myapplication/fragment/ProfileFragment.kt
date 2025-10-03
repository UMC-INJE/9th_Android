package com.umc.myapplication.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.umc.myapplication.R
import com.umc.myapplication.activity.EditProfileActivity
import com.umc.myapplication.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    companion object {val editProfileRequestCode = "editProfile"}
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonInit()
        val result = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == RESULT_OK){
                val data = result.data
                val name = data?.getStringExtra("name")
                binding.name.text = name
            }

        }
        binding.profileEditButton.setOnClickListener{
            val intent = Intent(activity, EditProfileActivity::class.java)
            result.launch(intent)
        }
    }
    fun buttonInit(){

        binding.order.icon.setImageResource(R.drawable.ic_profile_order)
        binding.order.label.text = "주문"

        binding.order.icon.setImageResource(R.drawable.ic_profile_pass)
        binding.order.label.text = "패스"

        binding.order.icon.setImageResource(R.drawable.ic_profile_event)
        binding.order.label.text = "이벤트"

        binding.order.icon.setImageResource(R.drawable.ic_profile_setting)
        binding.order.label.text = "설정"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}