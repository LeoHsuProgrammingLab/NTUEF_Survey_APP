package com.example.ntufapp.ui.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ntufapp.ui.theme.md_theme_light_primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NTUEFTopBar(
    onMenuClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "臺大實驗林調查APP",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clip(MaterialTheme.shapes.small),
                style = MaterialTheme.typography.displaySmall,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                   onMenuClick()
                }
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = md_theme_light_primary)
    )
}

