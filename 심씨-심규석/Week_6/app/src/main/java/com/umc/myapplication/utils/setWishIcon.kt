package com.umc.myapplication.utils

import android.widget.ImageView
import com.umc.myapplication.R

fun ImageView.setWishIcon(isWish: Boolean) {
    setImageResource(if (isWish) R.drawable.ic_red_heart else R.drawable.ic_bnv_wish_list)
}