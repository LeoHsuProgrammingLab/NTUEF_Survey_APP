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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.model.PlotData

@Composable
fun MetaDateView(newPlotData: PlotData) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MetaDataCol(info1 = Pair("調查日期", newPlotData.Date), info2 = Pair("營林區", newPlotData.ManageUnit), info3 = Pair("林班地", newPlotData.SubUnit))
        MetaDataCol(info1 = Pair("樣區名稱", newPlotData.PlotName), info2 = Pair("樣區編號", newPlotData.PlotNum.toString()), info3 = Pair("樣區型態", newPlotData.PlotType))
        MetaDataCol(info1 = Pair("樣區面積", newPlotData.PlotArea.toString()), info2 = Pair("TWD97_X", newPlotData.TWD97_X.toString()), info3 = Pair("TWD97_Y", newPlotData.TWD97_Y.toString()))
        MetaDataCol(info1 = Pair("海拔", newPlotData.Altitude.toString()), info2 = Pair("坡度", newPlotData.Slope.toString()), info3 = Pair("坡向", newPlotData.Aspect))
        Column(modifier = Modifier.padding(5.dp)) {
            SearchableDropdownMenu(newPlotData.Surveyor, defaultString = "名單", label = "樣區調查人員", dialogType = "Name", readOnly = true, onChoose = {}, onAdd = {})
            Divider(modifier = Modifier.padding(3.dp))
            SearchableDropdownMenu(newPlotData.HtSurveyor, defaultString = "名單", label = "樹高調查人員", dialogType = "Name", readOnly = true, onChoose = {} ) {}
        }
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
