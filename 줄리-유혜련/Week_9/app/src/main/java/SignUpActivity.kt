package com.umc.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.myapplication.data.db.SongDatabase
import com.umc.myapplication.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
            finish()
        }
    }

    private fun getUser() : User {
        val email : String = binding.signUpId.text.toString() + "@" + binding.signUpDirectInput.text.toString()
        val name: String = binding.signUpName.text.toString()
        val pwd : String = binding.signUpPassword.text.toString()

        return User(email, pwd, name)
    }

    private fun signUp() {
        if (binding.signUpId.text.toString().isEmpty() || binding.signUpDirectInput.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.signUpPassword.text.toString() != binding.signUpPasswordCheck.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val users = userDB.userDao().getUsers()
    }

}