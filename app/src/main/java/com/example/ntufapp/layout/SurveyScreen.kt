package com.example.ntufapp.layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree
import com.example.ntufapp.ui.CheckAddButton
import com.example.ntufapp.ui.ChipsTreeCondition
import com.example.ntufapp.ui.MetaDateView
import com.example.ntufapp.ui.PlotTreeView
import com.example.ntufapp.ui.SearchableDropdownMenu
import com.example.ntufapp.ui.theme.InputProgressView

//ref: https://www.youtube.com/watch?v=8XJfLaAOxD0&ab_channel=AndroidDevelopers
// live-edit for compose

@Composable
fun ReSurveyScreen(
    onNextButtonClick: (Int) -> Unit,
    oldPlotData: PlotData,
    newPlotData: PlotData
) {
    val contextNow = LocalContext.current
    // Save the final return data
    val newPlotDataState = remember {
        mutableStateOf(newPlotData)
    }
    
    newPlotDataState.value.setToday()

    // Save the tree numbers in ReSurvey Layout
    val treeNumList = mutableListOf<String>()
    for (i in 0 until newPlotData.PlotTrees.size) {
        treeNumList.add((i + 1).toString())
    }

    val totalTreesState = remember {
        mutableStateOf(treeNumList)
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        MetaDateView(newPlotData = newPlotDataState.value)
        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(400.dp)
        ) {
            Column(
                modifier = Modifier
                    .height(390.dp)
            ) {
                val curTreeNum = remember {
                    mutableStateOf("1")
                }

                Row {

                    PlotTreeView(curTreeNum = curTreeNum, totalTreesState = totalTreesState.value, newPlotData = newPlotDataState.value)

                    InputProgressView(
                        curTreeNum.value,
                        newPlotDataState.value
                    )

                    // first deal with the state of the PlotTrees in PlotData
                    // second pass the state into the SurveyProgress()
                    // Display the PlotTrees in the SurveyProgress()
                    // Inside InputBlock,
                    // pass four mutableState of the DBH, Ht, VisHt, ForkHt to change the value
                    // Then pass four mutableState into the SurveyProgress
                    // Think about the difference between directly changing the newPlotDataState and set a mutableState, then eventually assign to newPlotDataState

//                    SurveyProgress(newPlotDataState.value)
                }
            }
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(), verticalArrangement = Arrangement.Bottom){
            CheckAddButton()
        }
    }
}

@Composable
fun checkDialogue(treeData: Tree, onDismiss: () -> Unit) {

}

@Preview(device = Devices.PIXEL_C)
@Composable
fun PreviewMyApp() {

}




