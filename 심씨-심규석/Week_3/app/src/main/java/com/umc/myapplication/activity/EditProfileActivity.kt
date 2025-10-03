package com.umc.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.umc.myapplication.R
import com.umc.myapplication.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // 초기에는 비활성화
        binding.saveButton.isEnabled = false

        // 공통 TextWatcher
        val watcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val first = binding.firstName.text?.toString()?.trim().orEmpty()
                val last = binding.lastName.text?.toString()?.trim().orEmpty()
                binding.saveButton.isEnabled = first.isNotEmpty() && last.isNotEmpty()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        }
        binding.firstName.addTextChangedListener(watcher)
        binding.lastName.addTextChangedListener(watcher)

        binding.saveButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", binding.firstName.text.toString() + binding.lastName.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

}