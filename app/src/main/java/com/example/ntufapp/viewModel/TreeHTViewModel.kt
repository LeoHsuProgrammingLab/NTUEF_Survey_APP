package com.example.ntufapp.viewModel

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class TreeHTViewModel: ViewModel() {
    var htSurveyor = mutableStateOf("")
    var htDate = mutableStateOf("")
    var visHeight = mutableStateOf("")
    var measHeight = mutableStateOf("")
    var forkHeight = mutableStateOf("")
}