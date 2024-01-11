package com.example.ntufapp.utils

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun DisableBackButtonHandler(
    backDispatcher: OnBackPressedDispatcher,
    isEnabled: Boolean = true,
    onBackPressed: () -> Unit = {}
) {
    DisposableEffect(backDispatcher) {
        val callback = object : OnBackPressedCallback(isEnabled) {
            override fun handleOnBackPressed() {
                // Handle back button press
                onBackPressed()
            }
        }

        backDispatcher.addCallback(callback)

        onDispose {
            callback.remove()
        }
    }
}