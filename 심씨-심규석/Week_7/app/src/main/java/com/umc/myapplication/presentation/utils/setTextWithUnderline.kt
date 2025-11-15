package com.umc.myapplication.presentation.utils

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import com.umc.myapplication.R

@SuppressLint("ResourceAsColor")
fun setUnderlinedSpannable(
    textView: TextView,
    fullText: String,
    underlineTargets: List<String>,
    textColor: Int = R.color.black,
    textSizeSp: Float = 14f,
    clickActions: Map<String, (() -> Unit)> = emptyMap()
) {
    val spannable = SpannableString(fullText)
    underlineTargets.forEach { target ->
        val start = fullText.indexOf(target)
        if (start >= 0) {
            val end = start + target.length
            val underlineSpan = UnderlineSpan()
            spannable.setSpan(underlineSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // 클릭 동작 추가
            if (clickActions.containsKey(target)) {
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        clickActions[target]?.invoke()
                    }
                    @SuppressLint("ResourceAsColor")
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = textColor // 밑줄 링크도 동일 색상
                        ds.isUnderlineText = true
                    }
                }
                spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
    textView.text = spannable
    textView.setTextColor(textColor)
    textView.textSize = textSizeSp
    textView.movementMethod = LinkMovementMethod.getInstance()
}
