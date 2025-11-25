package com.umc.myapplication.presentation.utils

import android.util.Patterns
import java.time.LocalDate

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
