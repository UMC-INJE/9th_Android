package com.umc.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.myapplication.data.db.SongDatabase
import com.umc.myapplication.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity(), SignUpView {
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

//    private fun signUp() {
//        if (binding.signUpId.text.toString().isEmpty() || binding.signUpDirectInput.text.toString().isEmpty()) {
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if (binding.signUpPassword.text.toString() != binding.signUpPasswordCheck.text.toString()) {
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.userDao().insert(getUser())
//
//        val users = userDB.userDao().getUsers()
//    }

    private fun signUp() {
        if (binding.signUpId.text.toString().isEmpty() || binding.signUpDirectInput.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.signUpName.text.toString().isEmpty()) {
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.signUpPassword.text.toString() != binding.signUpPasswordCheck.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())

//        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
//        authService.signUp(getUser()).enqueue(object: Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                Log.d("SIGNUP-ACT/RESPONSE", response.toString())
//
//                val resp: AuthResponse = response.body()!!
//
//                when(resp.code) {
//                    1000 -> finish()
//                    2016, 2017 -> {
//                        binding.signUpEmailError.text = resp.message
//                        binding.signUpEmailError.visibility = View.VISIBLE
//                    }
//                    2018 -> {
//
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Log.d("SIGNUP-ACT/ERROR", t.message.toString())
//            }
//        })
        Log.d("SIGNUP-ACT/ASYNC", "Hello, FLO")


    }

    override fun onSignUpSuccess(memberId: Int) {
        Toast.makeText(this, "회원가입 성공! memberId = $memberId", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onSignUpFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}