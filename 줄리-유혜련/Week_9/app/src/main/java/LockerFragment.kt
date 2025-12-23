package com.umc.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.myapplication.LockorVPAdapter
import com.umc.myapplication.LoginActivity
import com.umc.myapplication.MainActivity
import com.umc.myapplication.databinding.FragmentLockerBinding
class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) 어댑터 연결
        val lockerAdapter = LockorVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter

        // 2) TayLayout 연결
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        binding.login.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    override fun onStart(){
        super.onStart()
        initViews()
    }
    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun initViews() {
        val jwt : Int = getJwt()
        if (jwt == 0) {
            binding.login.text = "로그인"
            binding.login.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else {
            binding.login.text = "로그아웃"
            binding.login.setOnClickListener {
                // 로그아웃
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun logout() {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("jwt")
        editor.apply()
    }
}