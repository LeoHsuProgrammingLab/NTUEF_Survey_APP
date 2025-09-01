package com.example.ntufapp.ui.widget.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.example.ntufapp.R
import com.example.ntufapp.ui.theme.Shapes
import com.example.ntufapp.ui.theme.basicModifier
import com.example.ntufapp.ui.widget.SearchableChooseMenu
import com.example.ntufapp.utils.showMessage

@Composable
fun AdjustSpeciesDialog( // Survey Screen
    speciesList: MutableState<List<String>>,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onNextButtonClick: (String) -> Unit
){
    val context = LocalContext.current
    val treeSpecies = remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(shape = Shapes.small) {
            Column(
                modifier = basicModifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchableChooseMenu(
                    totalItemsList = speciesList.value as MutableList<String>,
                    onChoose = {
                        treeSpecies.value = it
                    }
                )

                Spacer(modifier = basicModifier)

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Button(
                        modifier = basicModifier,
                        onClick = onCancelClick,
                    ) {
                        Text(stringResource(id = (R.string.cancel)))
                    }

                    Button(
                        modifier = basicModifier,
                        onClick = {
                            if(treeSpecies.value != "") {
                                onNextButtonClick(treeSpecies.value)
                            } else {
                                showMessage(context, "請選擇樹種!")
                            }
                        }
                    ) {
                        Text("確認樹種")
                    }
                }
            }
        }
    }
}
