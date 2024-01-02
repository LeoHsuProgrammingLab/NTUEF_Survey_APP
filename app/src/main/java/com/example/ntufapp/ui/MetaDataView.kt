package com.example.ntufapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.theme.CustomizedDivider
import com.example.ntufapp.ui.theme.IntervalDivider

@Composable
fun MetaDateView(newPlotData: PlotData) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val dateInfo = Pair("調查日期", newPlotData.Date)
        val mangeUnitInfo = Pair("營林區", newPlotData.ManageUnit)
        val subUnitInfo = Pair("林班", newPlotData.SubUnit)
        val plotNumInfo = Pair("樣區編號", newPlotData.PlotNum.toString())
        val plotNameInfo = Pair("樣區名稱", newPlotData.PlotName)
        val plotAreaInfo = Pair("樣區面積", newPlotData.PlotArea.toString())
        val plotTypeInfo = Pair("樣區型態", newPlotData.PlotType)
        val TWD97_X = Pair("TWD97_X", newPlotData.TWD97_X)
        val TWD97_Y = Pair("TWD97_Y", newPlotData.TWD97_Y)
        val altitudeInfo = Pair("海拔", newPlotData.Altitude.toString())
        val slopeInfo = Pair("坡度", newPlotData.Slope.toString())
        val aspectInfo = Pair("坡向", newPlotData.Aspect)

        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            MetaDataRow(info1 = dateInfo, info2 = mangeUnitInfo, info3 = subUnitInfo, info4 = altitudeInfo)
            MetaDataRow(info1 = plotNameInfo, info2 = plotNumInfo, info3 = plotTypeInfo, info4 = plotAreaInfo)
            MetaDataRow(info1 = slopeInfo, info2 = aspectInfo, info3 = TWD97_X, info4 = TWD97_Y)
        }

        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SearchableDropdownMenu(newPlotData.Surveyor, defaultString = "名單", label = "樣區調查人員", dialogType = "Name", readOnly = true, onChoose = {}, onAdd = {}, keyboardType = KeyboardType.Number)
            IntervalDivider()
            SearchableDropdownMenu(newPlotData.HtSurveyor, defaultString = "名單", label = "樹高調查人員", dialogType = "Name", readOnly = true, onChoose = {}, keyboardType = KeyboardType.Number ) {}
        }
    }

    Divider(modifier = Modifier.padding(vertical = 2.dp))
}

@Composable
fun MetaDataRow(info1: Pair<String, String>, info2: Pair<String, String>, info3: Pair<String, String>, info4: Pair<String, String>) {
    Row(modifier = Modifier.padding(5.dp)) {
        MetaDataBlock(blockName = info1.first, blockVal = info1.second)
        MetaDataBlock(blockName = info2.first, blockVal = info2.second)
        MetaDataBlock(blockName = info3.first, blockVal = info3.second)
        MetaDataBlock(blockName = info4.first, blockVal = info4.second)
    }
}

@Composable
fun MetaDataBlock(blockName: String, blockVal: String) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .width(240.dp)
            .height(40.dp)
    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            Text(
                "$blockName : $blockVal",
                fontSize = 20.sp,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}
