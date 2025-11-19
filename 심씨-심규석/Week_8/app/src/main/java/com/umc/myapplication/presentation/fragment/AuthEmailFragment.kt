package com.umc.myapplication.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.umc.myapplication.R
import com.umc.myapplication.databinding.FragmentAuthEmailBinding
import com.umc.myapplication.presentation.feature.AuthViewModel
import com.umc.myapplication.presentation.utils.setUnderlinedSpannable
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AuthEmailFragment : Fragment() {

    private val viewModel by activityViewModels<AuthViewModel>()
    private var _binding: FragmentAuthEmailBinding? = null
    private val binding get() = _binding!!

    // TextWatcher 등록 해제용
    private var emailWatcherJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthEmailBinding.inflate(inflater, container, false)

        // 고정 텍스트 꾸미기
        setUnderlinedSpannable(
            textView = binding.locationText,
            fullText = "대한민국 변경",
            underlineTargets = listOf("변경"),
            textColor = R.color.gray600
        )
        setUnderlinedSpannable(
            textView = binding.privacyPolicySummaryText,
            fullText = "계속 진행하면 나이키의  개인정보 처리방침 및 이용악관에 동의하게 됩니다.",
            underlineTargets = listOf("개인정보 처리방침 및 이용악관"),
            textColor = R.color.gray600
        )

        // 입력 변경 → ViewModel에 상태만 전달
        binding.emailInputLayout.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.updateEmail(text?.toString().orEmpty())
        }



        // LiveData observe (viewLifecycleOwner 사용)
        observeUiState()

        return binding.root
    }

    private fun observeUiState() {
        // 권장: onViewCreated 이후 viewLifecycleOwner로 관찰하여 View lifecycle에 안전하게 연결
        // repeatOnLifecycle은 Flow에 주로 쓰지만, 여기선 예시로 lifecycleScope + observe 혼용
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 이메일 입력값 UI ↔ 상태 동기화(복귀 시 반영)
                launch {
                    viewModel.form.observe(viewLifecycleOwner) { form ->
                        val edit = binding.emailInputLayout.editText
                        val current = edit?.text?.toString().orEmpty()
                        if (form.email != current) {
                            edit?.setText(form.email)
                            edit?.setSelection(form.email.length)
                        }
                    }
                }

                // 에러 표시: 회원가입 플로우 기준 signUpError 사용
                // 로그인 화면이라면 signInError를 observe 하세요.
                launch {
                    viewModel.signUpError.observe(viewLifecycleOwner) { msg ->
                        // 이메일 관련 에러를 TextInputLayout에 표시
                        // null이면 에러 제거됨
                        binding.emailInputLayout.error = msg
                        // 일부 레이아웃 이슈 시 requestLayout이 필요할 수 있음
                        // binding.emailInputLayout.requestLayout()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        emailWatcherJob?.cancel()
        _binding = null
    }
}
