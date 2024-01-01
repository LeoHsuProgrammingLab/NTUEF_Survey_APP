package com.example.ntufapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

val inputTextModifier = Modifier
    .padding(10.dp)
    .size(width = 120.dp, height = 60.dp)

val dropDownMenuModifier = Modifier
    .height(200.dp)

val dropDownItemModifier = Modifier
    .height(40.dp)

val circleThreeDigitsModifier = Modifier
    .padding(horizontal = 10.dp, vertical = 5.dp)
    .size(70.dp)
    .clip(CircleShape)
    .background(md_theme_light_inverseOnSurface)