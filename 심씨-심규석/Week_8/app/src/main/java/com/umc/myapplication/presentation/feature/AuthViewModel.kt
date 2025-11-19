package com.umc.myapplication.presentation.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.umc.myapplication.domain.auth.SignInWithEmailUseCase
import com.umc.myapplication.domain.auth.SignUpWithEmailUseCase
import com.umc.myapplication.domain.model.SignUpForm
import com.umc.myapplication.domain.model.User
import com.umc.myapplication.domain.validation.SignInParams
import com.umc.myapplication.domain.validation.SignInValidationUseCase
import com.umc.myapplication.domain.validation.SignUpValidationUseCase
import com.umc.myapplication.domain.validation.ValidationError
import com.umc.myapplication.domain.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val signUpWithEmail: SignUpWithEmailUseCase,
    private val signInWithEmail: SignInWithEmailUseCase,
    private val signInValidator: SignInValidationUseCase,
    private val signUpValidator: SignUpValidationUseCase
) : ViewModel() {

    private val _form = MutableLiveData(SignUpForm())
    val form: LiveData<SignUpForm> = _form

    private val _userId = MutableLiveData<String?>(null)
    val userId: LiveData<String?> = _userId

    private val _buttonEnabled = MutableLiveData(false)
    val buttonEnabled: LiveData<Boolean> = _buttonEnabled

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _signUpError = MutableLiveData<String?>()
    val signUpError: LiveData<String?> = _signUpError

    private val _signInError = MutableLiveData<String?>()
    val signInError: LiveData<String?> = _signInError

    private val _signedInUser = MutableLiveData<User?>()
    val signedInUser: LiveData<User?> = _signedInUser

    private val _signedUpUser = MutableLiveData<User?>()
    val signedUpUser: LiveData<User?> = _signedUpUser

    init {
        _userId.value = auth.currentUser?.uid
        auth.addAuthStateListener { fa ->
            _userId.postValue(fa.currentUser?.uid)
        }
    }

    fun refreshCurrentUser() {
        _userId.value = auth.currentUser?.uid
    }

    fun logOut() {
        auth.signOut()
        _userId.value = null
    }

    fun setUserId(id: String) { _userId.value = id }

    fun updateEmail(v: String) = update { copy(email = v) }
    fun updatePassword(v: String) = update { copy(password = v) }
    fun updateFirstName(v: String) = update { copy(firstName = v) }
    fun updateLastName(v: String) = update { copy(lastName = v) }
    fun updateBirth(v: String) = update { copy(birthDate = v) }

    private fun update(block: SignUpForm.() -> SignUpForm) {
        val new = block(_form.value ?: SignUpForm())
        _form.value = new
    }

    fun setButtonEnabled(enabled: Boolean) {
        _buttonEnabled.value = enabled
    }

    fun signIn(email: String, password: String) {
        when (val vr = signInValidator.execute(SignInParams(email, password))) {
            is ValidationResult.Invalid -> {
                _signInError.value = vr.errors.firstOrNull()?.let {
                    when (it) {
                        is com.umc.myapplication.domain.validation.ValidationError.Field -> it.message
                        else -> "로그인 정보를 확인하세요."
                    }
                } ?: "로그인 정보를 확인하세요."
                return
            }
            ValidationResult.Valid -> Unit
        }

        _loading.value = true
        _signInError.value = null
        _signedInUser.value = null

        viewModelScope.launch {
            val result = signInWithEmail(email.trim(), password)
            _loading.value = false
            result.fold(
                onSuccess = { user ->
                    _signedInUser.value = user
                    _userId.value = user.uid
                },
                onFailure = { e ->
                    _signInError.value = e.localizedMessage ?: "로그인에 실패했습니다."
                }
            )
        }
    }

    fun signUp() {
        val current = _form.value ?: SignUpForm()
        when (val vr = signUpValidator.execute(current)) {
            is ValidationResult.Invalid -> {
                _signUpError.value = vr.errors.firstOrNull()?.let {
                    when (it) {
                        is ValidationError.Field -> it.message
                        ValidationError.InvalidBirthDate -> "생년월일 형식을 확인하세요(yyyyMMdd)."
                        ValidationError.PasswordNotComplex -> "영문 대/소문자와 숫자를 포함해야 합니다."
                        ValidationError.PasswordTooShort -> "비밀번호는 8자 이상이어야 합니다."
                        ValidationError.InvalidEmail -> "이메일 형식을 확인하세요."
                    }
                } ?: "입력값을 확인하세요."
                return
            }
            ValidationResult.Valid -> Unit
        }

        _loading.value = true
        _signUpError.value = null
        _signedUpUser.value = null

        viewModelScope.launch {
            val result = signUpWithEmail(current.email.trim(), current.password)
            _loading.value = false
            result.fold(
                onSuccess = { user ->
                    _signedUpUser.value = user
                    _userId.value = user.uid
                },
                onFailure = { e ->
                    _signUpError.value = e.localizedMessage ?: "회원가입에 실패했습니다."
                }
            )
        }
    }
}
