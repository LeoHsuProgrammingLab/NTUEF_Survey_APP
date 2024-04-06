package com.example.ntufapp.ui.widget.dialog

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier
import com.example.ntufapp.ui.theme.md_theme_light_onPrimary
import com.example.ntufapp.ui.theme.md_theme_light_primary
import com.example.ntufapp.ui.widget.PlotSelectionDropDownMenu
import com.example.ntufapp.ui.widget.SearchableChooseMenu
import com.example.ntufapp.utils.showMessage


@Composable
fun DialogHeader(header: String) {
    Text(
        text = header,
        modifier = basicModifier,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    )
}

@Composable
fun GeneralConfirmDialog(
    reminder: String,
    confirmText: String = stringResource(id = (R.string.confirm)),
    cancelText: String = stringResource(id = (R.string.cancel)),
    leadingIcon: () -> Unit = {},
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() }
    ) {
        val text = remember { mutableStateOf("") }
        Surface(
            shape = Shapes.small
        ) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogHeader(header = reminder)
                Spacer(modifier = basicModifier)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = basicModifier,
                        onClick = onCancelClick
                    ) {
                        Text(cancelText)
                    }
                    Button(
                        modifier = basicModifier,
                        onClick = onConfirmClick
                    ) {
                        Text(confirmText)
                    }
                }
            }
        }
    }
}