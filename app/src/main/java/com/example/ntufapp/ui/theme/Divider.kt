package com.example.ntufapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DropdownDivider() {
    Divider(
        color = Color.Black,
        thickness = 1.dp,
        modifier = Modifier.padding(1.dp)
    )
}

@Composable
fun CustomizedDivider(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color.Gray)
    )
}
