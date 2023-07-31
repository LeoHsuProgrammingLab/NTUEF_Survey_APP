package com.example.ntufapp.layout

import androidx.compose.foundation.Image
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.ntufapp.R
import com.example.ntufapp.ui.NTUFTopBar
import com.example.ntufapp.ui.theme.NTUFAPPTheme
import com.example.ntufapp.viewModel.TreeViewModel

class StartPage: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NTUFAPPTheme(){
                Surface(modifier = Modifier.fillMaxSize()){
                    StartApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartApp() {
    Scaffold(
        topBar = {
            NTUFTopBar()
        },
        content = {

            Column(
                modifier = Modifier.padding(it).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
//                Spacer(Modifier.padding(40.dp))

                val image = painterResource(id = R.drawable.forest_svgrepo_com)
                Image(painter = image,
                    contentDescription = "forest start screen",
                    modifier = Modifier.size(200.dp, 200.dp)
                        .clip(CircleShape)
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(){
                    Button(modifier = Modifier.padding(end = 20.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Text("複查樣區", fontSize = 20.sp)
                    }

                    Button(
                        onClick = { /*TODO*/ }
                    ) {
                        Text("新增樣區", fontSize = 20.sp)
                    }
                }
            }
        }
    )
}