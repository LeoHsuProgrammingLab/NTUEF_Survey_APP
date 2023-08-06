package com.example.ntufapp.layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.model.Tree
import com.example.ntufapp.ui.SearchableDropdownMenu
import com.example.ntufapp.ui.TreeStateMenu

//ref: https://www.youtube.com/watch?v=8XJfLaAOxD0&ab_channel=AndroidDevelopers
// live-edit for compose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReSurveyScreen(
    onNextButtonClick: (Int) -> Unit,
    oldPlotData: PlotData,
    newPlotData: PlotData
) {
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

    //

    Column(modifier = Modifier.padding(10.dp)) {
        MetaDateView(newPlotData = newPlotDataState.value)
        SearchableDropdownMenu(totalTreesState.value, "請選擇樣樹")
        SearchableDropdownMenu(DataSource.SpeciesList, newPlotDataState.value.searchTree(1).Species)

        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(), verticalArrangement = Arrangement.Bottom){
            CheckAddButton()
        }
    }
}

@Composable
fun CheckAddButton(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        OutlinedButton(
            onClick = { /* Handle second button click */ },
            modifier = Modifier.padding(end = 16.dp)//.clip(MaterialTheme.shapes.small)
        ) {
            Text("完成此次調查", fontSize = 20.sp)
        }

        OutlinedButton(
            onClick = {
                //Todo
            },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Text("修改前一筆", fontSize = 20.sp)
        }

        OutlinedButton(
            onClick = { /* Handle second button click */ },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Text("新增", fontSize = 20.sp)
        }
    }
}

@Composable
fun MetaDateView(newPlotData: PlotData) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MetaDataCol(info1 = Pair("調查日期", newPlotData.Date), info2 = Pair("營林區", newPlotData.ManageUnit), info3 = Pair("林班地", newPlotData.SubUnit))
        MetaDataCol(info1 = Pair("樣區名稱", newPlotData.PlotName), info2 = Pair("樣區編號", newPlotData.PlotNum.toString()), info3 = Pair("樣區型態", newPlotData.PlotType))
        MetaDataCol(info1 = Pair("樣區面積", newPlotData.PlotArea.toString()), info2 = Pair("TWD97_X", newPlotData.TWD97_X.toString()), info3 = Pair("TWD97_Y", newPlotData.TWD97_Y.toString()))
        MetaDataCol(info1 = Pair("海拔", newPlotData.Altitude.toString()), info2 = Pair("坡度", newPlotData.Slope.toString()), info3 = Pair("坡向", newPlotData.aspect))
        Column() {
            TreeStateMenu("Test", DataSource.SurveyorList)
        }
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        //TODO
    }

    Divider(modifier = Modifier.padding(vertical = 2.dp))
}

@Composable
fun MetaDataCol(info1: Pair<String, String>, info2: Pair<String, String>, info3: Pair<String, String>) {
    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        MetaDataRow(info = info1)
        MetaDataRow(info = info2)
        MetaDataRow(info = info3)
    }
}

@Composable
fun MetaDataRow(info: Pair<String, String>) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .width(250.dp)
            .height(50.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Text(info.first + " : " + info.second, fontSize = 20.sp, fontWeight = FontWeight.W700, modifier = Modifier.padding(2.dp))
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




