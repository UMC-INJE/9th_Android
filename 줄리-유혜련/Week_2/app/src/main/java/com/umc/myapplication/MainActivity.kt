package com.umc.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.umc.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        const val RESULT_ALBUM_TITLE = "result_album_title"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var songResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val albumTitle = result.data?.getStringExtra(RESULT_ALBUM_TITLE)
                if (!albumTitle.isNullOrBlank()) {
                    Toast.makeText(this, "선택한 앨범: $albumTitle", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val song = Song(binding.miniSongTitleTv.text.toString(),binding.miniSongArtistTv.text.toString())

        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))
            val intent = Intent(this,SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            songResultLauncher.launch(intent)
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
