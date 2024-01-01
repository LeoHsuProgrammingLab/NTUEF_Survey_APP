package com.example.ntufapp.data

import android.app.Application
import androidx.compose.ui.unit.dp

class ntufappInfo: Application() {
    companion object{
        const val tag = "NTUF"
        const val dTag = "NTUF_DEBUG"
        const val defaultTreeNum = 60
        const val dbhThreshold = 0.02
        const val htThreshold = 0.1
        const val changeDataQuota = 3
        const val outputDirName = "NTUEF_APP"
    }
}