package com.umc.myapplication.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.umc.myapplication.R
import com.umc.myapplication.presentation.activity.EditProfileActivity
import com.umc.myapplication.databinding.FragmentProfileBinding
import com.umc.myapplication.presentation.feature.AuthViewModel

class ProfileFragment : Fragment() {
    companion object {val editProfileRequestCode = "editProfile"}
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel : AuthViewModel by activityViewModels()
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
        val user = viewModel.signedInUser.value
        binding.name.text = user?.name ?: "유저"
        val url = user?.profileUrl
        if (url.isNullOrBlank()) {
            // 기본 이미지
            binding.profileImage.setImageResource(R.drawable.img_basic_profile)
        } else {
            Glide.with(binding.root)
                .load(url)
                .placeholder(R.drawable.img_basic_profile)
                .error(R.drawable.img_basic_profile)
                .circleCrop()
                .into(binding.profileImage)
        }

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
        binding.logoutButton.setOnClickListener {
            viewModel.logOut()
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