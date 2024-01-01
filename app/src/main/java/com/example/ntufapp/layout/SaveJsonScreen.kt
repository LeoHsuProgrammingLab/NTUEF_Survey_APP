package com.example.ntufapp.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.R
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.SaveJsonDialog
import com.example.ntufapp.ui.theme.LayoutDivider
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.lightBorder
import com.example.ntufapp.ui.theme.md_theme_light_inverseOnSurface
import com.example.ntufapp.utils.saveJsonFile
import com.google.relay.compose.BoxScopeInstanceImpl.align
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun SaveJsonScreen(
    newPlotData: PlotData,
    onBackButtonClick: () -> Unit,
) {
    val context = LocalContext.current
    val modifier = Modifier.padding(10.dp)
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val image = painterResource(id = R.drawable.database_download_svgrepo_com)
        Image(painter = image,
            contentDescription = "save data screen",
            modifier = Modifier.size(200.dp, 200.dp)
        )
        LayoutDivider()
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val showDialog = remember { mutableStateOf(false) }

            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    modifier = modifier,
                    onClick = onBackButtonClick
                ) {
                    Text(text = "返回主頁", fontSize = 20.sp)
                }
                OutlinedButton(
                    modifier = modifier,
                    onClick = { showDialog.value = true }
                ) {
                    Text(text = "儲存樣區資料", fontSize = 20.sp)
                }

                if (showDialog.value) {
                    SaveJsonDialog(
                        onDismiss = { showDialog.value = false },
                        onSaveClick = {
                            showDialog.value = false
                            saveJsonFile(newPlotData, it, context)
                        },
                        onCancelClick = { showDialog.value = false }
                    )
                }
            }
        }
    }
}

