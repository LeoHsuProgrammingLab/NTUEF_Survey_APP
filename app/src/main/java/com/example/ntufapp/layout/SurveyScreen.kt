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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ntufapp.model.TreeData
import com.example.ntufapp.ui.NTUFTopBar
import com.example.ntufapp.ui.TextBox
import com.example.ntufapp.ui.theme.NTUFAPPTheme
import com.example.ntufapp.ui.treeStateMenu
import com.example.ntufapp.viewModel.TreeViewModel

//ref: https://www.youtube.com/watch?v=8XJfLaAOxD0&ab_channel=AndroidDevelopers
// live-edit for compose

class SurveyScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NTUFAPPTheme(darkTheme = true){
                Surface(modifier = Modifier.fillMaxSize()){

                }
            }
        }
    }
}

//@Composable
//fun SurveyAnswer(){
//    Row {
//        Image(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "")
//        Text(text = "Spark", color = MaterialTheme.colorScheme.secondary/*Color(0xFFEA80FC)*/)
//        //Color, Typography, Shape
////        Button()
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NTUFApp() {

    val treeViewModel = TreeViewModel()

    Scaffold(
        topBar = {
            Column{
                NTUFTopBar()
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        },

        content = {
            Column(modifier = Modifier.padding(it)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextBox("試驗地編號", defaultValue = "9", readOnly = true, textValue = treeViewModel.siteNum)
                    TextBox("樣區編號", defaultValue = "73-1", textValue = treeViewModel.plotNum)
                    TextBox("樣樹編號", defaultValue = "14", textValue = treeViewModel.plotNum)

                }
                Row {
                    TextBox("胸徑(DBH）", textValue = treeViewModel.dbh)
                    treeStateMenu("生長狀態", treeState = treeViewModel.state)
                }
//                HeightBlock(treeViewModel.treeHTVM)

                Column(modifier = Modifier.fillMaxHeight().fillMaxWidth(), verticalArrangement = Arrangement.Bottom){
                    CheckAddButton()
                }
            }
        }
    )
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
fun checkDialogue(treeData: TreeData, onDismiss: () -> Unit) {

}

//@Composable
//fun HeightBlock(treeHTVM: TreeHTViewModel) {
//    Row{
//        Box(
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .size(width = 80.dp, height = 100.dp),
//            contentAlignment = Alignment.TopCenter
//        ) {
//            Text("樹高", fontSize = 30.sp, fontWeight = FontWeight.Bold)//textAlign = TextAlign.Center
//        }
//        Box(
//            modifier = Modifier
//                .size(width = 600.dp, height = 350.dp)
//                .border(5.dp, Color.Black)
//        ) {
//            Row(modifier = Modifier.padding(5.dp)) {
//                Column {
//                    TextBox(label = "調查人員", keyboardTypeInput = KeyboardType.Text)
//                    TextBox(label = "目視樹高")
//                    TextBox(label = "實測樹高")
//                }
//                Column {
//                    TextBox(label = "調查日期")
//                    TextBox(label = "分岔樹高")
//                }
//            }
//
//        }
//    }
//}

@Preview(device = Devices.PIXEL_C)
@Composable
fun PreviewMyApp() {
    NTUFApp()
}




