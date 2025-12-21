package com.umc.myapplication.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.myapplication.SignUpActivity
import com.umc.myapplication.data.db.SongDatabase
import com.umc.myapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun login() {
        if (binding.loginId.text.toString().isEmpty() || binding.loginDirectInput.text.toString()
                .isEmpty()
        ) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.loginPassword.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email: String =
            binding.loginId.text.toString() + "@" + binding.loginDirectInput.text.toString()
        val pwd: String = binding.loginPassword.text.toString()

        val songDB = SongDatabase.Companion.getInstance(this)!!
        val user = songDB.userDao().getUser(email, pwd)

        user?.let {
            saveJwt(user.id)

            startMainActivity()
        }

        Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }
    }