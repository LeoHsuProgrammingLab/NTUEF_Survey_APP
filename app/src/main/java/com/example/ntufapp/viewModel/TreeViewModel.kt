package com.example.ntufapp.viewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.ntufapp.model.TreeHeight


class TreeViewModel: ViewModel() {
    var siteNum = mutableStateOf("")
    var plotNum = mutableStateOf("")
    var sampleNum = mutableStateOf("")
    var state = mutableStateOf("")
    var species = mutableStateOf("")
    var dbh = mutableStateOf("")
    
}