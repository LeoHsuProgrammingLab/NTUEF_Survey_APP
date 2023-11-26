package com.example.ntufapp

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ntufapp.ui.WindowInfo
import com.example.ntufapp.ui.rememberWindowInfo
import com.example.ntufapp.ui.theme.NTUFAPPTheme

class MainActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NTUFAPPTheme{
//                val windowInfo = rememberWindowInfo()
                Surface(modifier = Modifier.fillMaxSize()) {
                    NTUEFApp()
//                    ChipsTreeCondition()
//                    SurveyorAddDeleteMenu()
//                    TreeStateMenu("High", DataSource.SpeciesList)
                }
            }
        }
    }
}