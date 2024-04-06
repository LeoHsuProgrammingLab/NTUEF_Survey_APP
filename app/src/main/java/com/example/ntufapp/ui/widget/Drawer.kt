package com.example.ntufapp.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun Drawer(
    onNavSurveyScreen: () -> Unit,
    onNavLoadJsonScreen: () -> Unit
) {

}
