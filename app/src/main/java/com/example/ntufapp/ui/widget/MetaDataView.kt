package com.example.ntufapp.ui.widget

import android.util.Log
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

@Composable
fun MetaDateView(newPlotData: PlotData) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val dateInfo = Pair("調查日期", newPlotData.Date)
        val mangeUnitInfo = Pair("試驗地", newPlotData.ManageUnit)
        val subUnitInfo = Pair("林班", newPlotData.area_compart)
        val plotNumInfo = Pair("樣區編號", newPlotData.PlotNum)
        val plotNameInfo = Pair("樣區種類", newPlotData.PlotName)
        val plotAreaInfo = Pair("樣區面積(m2)", newPlotData.PlotArea.toString())
        val plotTypeInfo = Pair("樣區型態", newPlotData.PlotType)
        val TWD97_X = Pair("TWD97_X", newPlotData.TWD97_X)
        val TWD97_Y = Pair("TWD97_Y", newPlotData.TWD97_Y)
        val altitudeInfo = Pair("海拔(m)", newPlotData.Altitude.toString())
        val slopeInfo = Pair("坡度", newPlotData.Slope.toString())
        val aspectInfo = Pair("坡向", newPlotData.Aspect)

        Column(
            modifier = Modifier.padding(5.dp)
        ) {
//            MetaDataRow(info1 = dateInfo, info2 = mangeUnitInfo, info3 = subUnitInfo, info4 = altitudeInfo)
            MetaDataRowThreeCol(info1 = dateInfo, info2 = mangeUnitInfo, info3 = subUnitInfo)
            MetaDataRow(info1 = plotNameInfo, info2 = plotNumInfo, info3 = plotTypeInfo, info4 = altitudeInfo)
            MetaDataRow(info1 = slopeInfo, info2 = aspectInfo, info3 = TWD97_X, info4 = TWD97_Y)
        }

        Column(
            modifier = Modifier.padding(end = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SearchableChooseCheckMenu(
                newPlotData.userList.map { it.user_code + ": " + it.user_name }.toMutableList(),
                SelectionMode.MULTIPLE,
                defaultString = "名單",
                label = "樣區調查人員",
                readOnly = true,
                checkable = true,
                onChoose = {
                    // For Read-Only Menu
                },
                onUpdateList = { stringList ->
                    // For Choosable Menu
                    newPlotData.Surveyor = stringList.associate { it.split(":")[0].toInt() to it.split(":")[1] } as MutableMap<Int, String>
                }
            )
            SearchableChooseCheckMenu(
                newPlotData.userList.map { it.user_code + ": " + it.user_name }.toMutableList(),
                SelectionMode.SINGLE,
                searchable = false,
                defaultString = "名單",
                label = "樹高調查人員",
                readOnly = true,
                checkable = true,
                onChoose = {
                    // For Read-Only Menu
                },
                onUpdateList = {
                    // For Choosable Menu
                    newPlotData.HtSurveyor = it[0].split(": ")[0].toInt() to it[0].split(": ")[1]
                },
            )
        }
    }

    Divider(modifier = Modifier.padding(vertical = 2.dp))
}

@Composable
fun MetaDataRow(info1: Pair<String, String>, info2: Pair<String, String>, info3: Pair<String, String>, info4: Pair<String, String>) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MetaDataBlock(blockName = info1.first, blockVal = info1.second)
        MetaDataBlock(blockName = info2.first, blockVal = info2.second)
        MetaDataBlock(blockName = info3.first, blockVal = info3.second)
        MetaDataBlock(blockName = info4.first, blockVal = info4.second)
    }
}

@Composable
fun MetaDataRowThreeCol(info1: Pair<String, String>, info2: Pair<String, String>, info3: Pair<String, String>) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MetaDataBlock(blockName = info1.first, blockVal = info1.second)
        MetaDataBlock(blockName = info2.first, blockVal = info2.second, width = 480 + 10) // width + 2 * padding
        MetaDataBlock(blockName = info3.first, blockVal = info3.second)
    }
}

@Composable
fun MetaDataBlock(blockName: String, blockVal: String, width: Int = 240, height: Int = 40) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .width(width.dp)
            .height(height.dp)
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
