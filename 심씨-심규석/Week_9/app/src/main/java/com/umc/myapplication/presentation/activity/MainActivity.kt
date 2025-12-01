package com.umc.myapplication.presentation.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.umc.myapplication.BuildConfig
import com.umc.myapplication.R
import com.umc.myapplication.data.mock.testProductRepository
import com.umc.myapplication.databinding.ActivityMainBinding
import com.umc.myapplication.presentation.feature.AuthViewModel
import com.umc.myapplication.presentation.feature.UiProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(
) {
    private val TAG = "MainActivity"
    private val viewModel by viewModels<AuthViewModel>()
    private val uiViewModel: UiProductViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //테스트 데이터 업데이트
        uiViewModel.upsertProductList(testProductRepository.products)
        uiViewModel.upsertCategorieList(testProductRepository.categories)
        //로그아웃
        //viewModel.logOut()

        Log.d(TAG, "onCreate: ${BuildConfig.SERVER_URL}")

        //자동로그인
        viewModel.refreshCurrentUser()
        //FragmentContainerView에 연결된 NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // userId 변화에 따라 그래프/네비게이션 전환
        viewModel.accessToken.observe(this) { id ->
            if (id.isNullOrEmpty()) {
                // 비로그인: 웰컴 그래프 로드, BottomNav 숨김
                navController.setGraph(R.navigation.nav_graph_welcome)
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                // 로그인: 메인 그래프 로드, BottomNav 표시
                navController.setGraph(R.navigation.nav_graph_main)
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        // BottomNavigationView를 NavController와 연결
        binding.bottomNavigationView.setupWithNavController(navController)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            val destId = item.itemId

            val options = androidx.navigation.navOptions {
                // 해당 탭의 루트로 스택 정리
                popUpTo(destId) { inclusive = false }
                // 같은 목적지로 다시 이동할 때 중복 생성 방지
                launchSingleTop = true
            }

            // 현재 목적지와 같으면 굳이 네비게이션하지 않도록 가드
            if (navController.currentDestination?.id != destId) {
                navController.navigate(destId, null, options)
            }
            true
        }
    }
}