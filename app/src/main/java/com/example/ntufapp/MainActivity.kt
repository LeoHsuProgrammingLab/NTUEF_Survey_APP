package com.example.ntufapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ntufapp.data.DataSource
import com.example.ntufapp.layout.showMessage
import com.example.ntufapp.ui.SearchDropdownMenu
import com.example.ntufapp.ui.SearchableDropdownMenu
import com.example.ntufapp.ui.TreeStateMenu
import com.example.ntufapp.ui.theme.NTUFAPPTheme

class MainActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NTUFAPPTheme() {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NTUEFApp()
//                    SurveyorAddDeleteMenu()
//                    TreeStateMenu("High", DataSource.SpeciesList)
                }
            }
        }
    }
}