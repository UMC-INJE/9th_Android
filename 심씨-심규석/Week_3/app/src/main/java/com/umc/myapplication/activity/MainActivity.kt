package com.umc.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.umc.myapplication.R
import com.umc.myapplication.databinding.ActivityMainBinding
import com.umc.myapplication.utils.makeToast

class MainActivity : AppCompatActivity() {

    companion object{
        const val RECIEVED_MAIN_FROM_SONG_ACTIVITY = "recieved_to_main_activity"
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavController 가져오기

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        binding.mainBnv.setupWithNavController(navController)
        //목록누르면 액티비티 바꾸기




        //songActivity에서 결과받아서 출력하기
        val activityResultLauncher  = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val returnString = result.data?.getStringExtra(RECIEVED_MAIN_FROM_SONG_ACTIVITY)
                val title = result.data?.getStringExtra("title")
                val artist = result.data?.getStringExtra("artist")
                makeToast(this, "돌아온 값 : $returnString $title $artist")
            }
        }

        binding.songList.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            // 필요 시 추가 데이터
             intent.putExtra("title", binding.songTitle.text.toString())
            intent.putExtra("artist", binding.songArtist.text.toString())
            activityResultLauncher.launch(intent)
        }


        // Inset 적용 (필요 시)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }
    }
}