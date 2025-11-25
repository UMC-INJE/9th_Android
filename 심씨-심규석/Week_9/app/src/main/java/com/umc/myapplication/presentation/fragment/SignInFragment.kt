package com.umc.myapplication.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentAuthBinding
import com.umc.myapplication.domain.model.AuthScreenUiConfig
import com.umc.myapplication.presentation.utils.applyUiForDestination
import com.umc.myapplication.presentation.feature.AuthViewModel

class SignInFragment : Fragment() {

    // ViewModel & ViewBinding
    private val viewModel by activityViewModels<AuthViewModel>()
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)

        // Child NavHost 및 NavController
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph_auth)

        // 목적지별 UI 설정 맵
        val uiConfigs = buildUiConfigs(navController)

        // 버튼 활성화 상태 관찰
        viewModel.buttonEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.button.isEnabled = enabled
            binding.button.isActivated = enabled
            binding.button.alpha = if (enabled) 1f else 0.5f
            binding.button.setTextColor(
                if (enabled) {
                    resources.getColorStateList(R.color.white)
                } else {
                    resources.getColorStateList(R.color.gray600)
                }
            )
        }

        // 목적지 변경 리스너
        val listener = NavController.OnDestinationChangedListener { _, dest, _ ->
            applyUiForDestination(
                binding = binding,
                destinationId = dest.id,
                configs = uiConfigs
            )
        }
        navController.addOnDestinationChangedListener(listener)

        // 생명주기 정리
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                navController.removeOnDestinationChangedListener(listener)
            }
        })
        navController.currentDestination?.let { dest ->
            applyUiForDestination(binding = binding, destinationId = dest.id, configs = uiConfigs)
        }

        return binding.root
    }

    // 목적지별 UI 정책 맵
    private fun buildUiConfigs(
        navController: NavController
    ): Map<Int, AuthScreenUiConfig> {
        return mapOf(
            R.id.EmailFragment to AuthScreenUiConfig(
                title = "가입 또는 로그인을 위해\n" +
                        "이메일을 입력하세요",
                buttonText = "다음",
                onClick = {
                    navController.navigate(R.id.action_Email_to_PassWard)
                    viewModel.setButtonEnabled(false)}
            ),
            R.id.PassWordFragment to AuthScreenUiConfig(
                title = "이제 나이키 멤버가 되어볼까요?",
                buttonText = "로그인",
                onClick = {
                    //viewModel값 로그인 통신 넘기기
                    viewModel.signIn()
                    navController.navigate(R.id.action_PassWordFragment_to_EmptyFragment)
                }
            ),
            R.id.EmptyFragment to AuthScreenUiConfig(
                title = "성공적으로 로그인이 완료되었습니다.",
                buttonText = "계속",
                onClick = {
                }
            ),
            // 필요 시 목적지 추가
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
