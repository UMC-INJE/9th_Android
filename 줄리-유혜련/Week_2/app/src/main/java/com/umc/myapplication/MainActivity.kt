package com.umc.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.navigation.NavigationBarView
import com.umc.myapplication.databinding.ActivityMainBinding
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainPlayerCl.setOnClickListener {
            startActivity(Intent(this, SongActivity::class.java))
//            activityResultLauncher.launch(intent)
        }
        initBottomNavigation()

        if (savedInstanceState == null) {
            binding.mainBnv.selectedItemId = R.id.homeFragment
        }
    }

    private fun initBottomNavigation() {
        binding.mainBnv.setOnItemSelectedListener(onNavSelected)
    }

    private val onNavSelected = NavigationBarView.OnItemSelectedListener { item ->
        val fragment = when (item.itemId) {
            R.id.homeFragment   -> HomeFragment()
            R.id.lookFragment   -> LookFragment()
            R.id.searchFragment -> SearchFragment()
            R.id.lockerFragment -> LockerFragment()
            else -> return@OnItemSelectedListener false
        }

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.main_frm, fragment)
            .commit()

        true
    }
}
