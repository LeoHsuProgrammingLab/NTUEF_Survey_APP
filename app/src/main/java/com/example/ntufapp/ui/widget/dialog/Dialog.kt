package com.example.ntufapp.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier



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