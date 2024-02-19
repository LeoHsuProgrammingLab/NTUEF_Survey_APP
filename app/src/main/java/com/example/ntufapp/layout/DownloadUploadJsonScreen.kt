package com.example.ntufapp.layout

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ntufapp.R
import com.example.ntufapp.api.RetrofitInstance
import com.example.ntufapp.api.catalogueApi
import com.example.ntufapp.api.dataType.Connection
import com.example.ntufapp.api.systemCodeApi
import com.example.ntufapp.ui.theme.LayoutDivider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException

@Composable
fun DownloadUploadJsonScreen () {
    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = true) {}
    val tag = "LoadJsonScreen"

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//                Spacer(Modifier.padding(40.dp))
        val image = painterResource(id = R.drawable.forest_mountain_svgrepo_com)
        Image(
            painter = image,
            contentDescription = "forest start screen",
            modifier = Modifier.size(200.dp, 200.dp)
//                        .border(2.dp, Color.Black, CircleShape)
        )
        LayoutDivider()
        Button(
            onClick = {
                catalogueApi(coroutineScope, tag)
                systemCodeApi(coroutineScope, tag)
            }
        ) {
            Text("Get JSON Response")
        }
    }
}