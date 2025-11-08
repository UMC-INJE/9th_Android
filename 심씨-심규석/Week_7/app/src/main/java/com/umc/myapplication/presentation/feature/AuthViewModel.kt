package com.umc.myapplication.presentation.feature

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.myapplication.domain.model.SignUpForm
import java.time.LocalDate

class AuthViewModel : ViewModel() {
    private val _form = MutableLiveData(SignUpForm())
    val form: LiveData<SignUpForm> = _form
    private val _userId : MutableLiveData<String?> = MutableLiveData(null)
    val userId : LiveData<String?> = _userId

    fun setUserId(id : String){
        _userId.value = id
    }
    private val _buttonEnabled = MutableLiveData(false)
    val buttonEnabled: LiveData<Boolean> = _buttonEnabled

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
}

    fun isValidEmail(s: String): Boolean =
        s.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(s).matches()


    fun isValidLength(s: String): Boolean =
        s.length >= 8

    fun containsUpperLowerAndDigit(s: String): Boolean =
        Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$").matches(s)


    fun isValidBirth(s: String): Boolean {
        if (!Regex("^\\d{8}$").matches(s)) return false
        val yyyy = s.substring(0, 4).toInt()
        val mm = s.substring(4, 6).toInt()
        val dd = s.substring(6, 8).toInt()
        if (mm !in 1..12) return false
        return try {
            LocalDate.of(yyyy, mm, dd)
            true
        } catch (e: Exception) {
            false
        }
    }