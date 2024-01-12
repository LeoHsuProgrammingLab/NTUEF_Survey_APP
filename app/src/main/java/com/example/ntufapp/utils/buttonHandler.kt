package com.example.ntufapp.utils

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun DisableBackButtonHandler(
    backDispatcher: OnBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher!!,
    isEnabled: Boolean = true, // whether the back button handler is enabled or not
    onBackPressed: () -> Unit = {}
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
    val callback = remember {
        object : OnBackPressedCallback(isEnabled) {
            override fun handleOnBackPressed() {
                // Handle back button press
                currentOnBackPressed()
            }
        }
    }
    val lifeCycleOwner = LocalOnBackPressedDispatcherOwner.current
    DisposableEffect(lifeCycleOwner, backDispatcher) {
        backDispatcher.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }
}