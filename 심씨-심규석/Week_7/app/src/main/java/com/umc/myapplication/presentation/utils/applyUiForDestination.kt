package com.umc.myapplication.presentation.utils

import com.umc.myapplication.databinding.FragmentAuthBinding
import com.umc.myapplication.domain.model.AuthScreenUiConfig
import com.umc.myapplication.domain.model.defaultAuthScreenUiConfig

fun applyUiForDestination(
    binding: FragmentAuthBinding,
    destinationId: Int,
    configs: Map<Int, AuthScreenUiConfig>,
    defaultConfig: AuthScreenUiConfig = defaultAuthScreenUiConfig
) {
    val config = configs[destinationId] ?: defaultConfig
    binding.title.text = config.title
    binding.button.text = config.buttonText
    binding.button.setOnClickListener(config.onClick)
}