package com.example.ntufapp.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ntufapp.model.PlotData

@Composable
fun SurveyProgress(newPlotState: MutableState<PlotData>) {
    Box(
        modifier = Modifier
            .padding(20.dp)
            .background(md_theme_light_primaryContainer, RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .background(md_theme_light_primaryContainer)
        ) {
            itemsIndexed(newPlotState.value.PlotTrees) { idx, tree ->
                Row(
                    modifier = Modifier.padding(5.dp)
                ) {
                    ListItem(
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .clip(CircleShape)
                            .background(md_theme_light_inverseOnSurface),
                        headlineContent = {
                            Text(
                                tree.SampleNum.toString()
                            )
                        }
                    )
                    Row(
                        modifier = Modifier.padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val mod = Modifier.padding(5.dp)
                            .size(width = 90.dp, height = 50.dp)

                        TextField(value = "${tree.DBH}", onValueChange = {}, label = {Text("DBH")}, modifier = mod, readOnly = true)
                        TextField(value = "${tree.VisHeight}", onValueChange = {}, label = {Text("目視樹高")}, modifier = mod, readOnly = true)
                        TextField(value = "${tree.MeasHeight}", onValueChange = {}, label = {Text("量測樹高")}, modifier = mod, readOnly = true)
                        TextField(value = "${tree.ForkHeight}", onValueChange = {}, label = {Text("分岔樹高")}, modifier = mod, readOnly = true)
                    }
                }

                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.padding(1.dp)
                )
            }
        }
    }
}