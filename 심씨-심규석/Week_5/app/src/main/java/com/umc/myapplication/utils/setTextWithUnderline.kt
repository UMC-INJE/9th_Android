package com.umc.myapplication.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.widget.TextView

fun TextView.setTextWithUnderline(
    base: String,
    underlined: String,
    color: Int? = null
): SpannableString {
    // base 바로 뒤에 underlined를 붙인다
    val full = base + underlined
    val start = full.length - underlined.length
    val end = full.length

    val text = SpannableString(full).apply {
        setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (color != null) {
            setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    return text
}