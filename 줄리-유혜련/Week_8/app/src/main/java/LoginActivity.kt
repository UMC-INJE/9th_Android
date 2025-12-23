package com.umc.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.umc.myapplication.SignUpActivity
import com.umc.myapplication.data.db.SongDatabase
import com.umc.myapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
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

        val authService = AuthService()
        authService.setLoginView(this)
        authService.login(getUser())
    }

//        val songDB = SongDatabase.Companion.getInstance(this)!!
//        val user = songDB.userDao().getUser(email, pwd)
//
//        user?.let { user ->
//            saveJwt(user.id)
//            startMainActivity()
//        } ?: run {
//            Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
//        }

    private fun getUser(): User {
        val email = binding.loginId.text.toString() + "@" + binding.loginDirectInput.text.toString()
        val password = binding.loginPassword.text.toString()

        return User(email = email, password = password, name = "")
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveJwt(jwt: String) {
        val spf = getSharedPreferences("auth" , MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    private fun saveJwt2(jwt: String) {
        val spf = getSharedPreferences("auth2" , MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    override fun onLoginSuccess(code : String , data: AuthData) {
        val token = data.accessToken ?: ""
        saveJwt(token)
        Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
        startMainActivity()
    }

    override fun onLoginFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}