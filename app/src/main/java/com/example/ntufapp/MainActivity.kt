package com.example.ntufapp

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ntufapp.router.NTUEFApp
import com.example.ntufapp.ui.theme.NTUFAPPTheme

class MainActivity: AppCompatActivity(){
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NTUFAPPTheme{
                Surface(modifier = Modifier.fillMaxSize()) {
                    NTUEFApp()
                }
            }
        }
    }
}