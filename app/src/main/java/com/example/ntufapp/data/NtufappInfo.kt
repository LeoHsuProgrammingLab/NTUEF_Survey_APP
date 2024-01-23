package com.example.ntufapp.data

import android.app.Application
import android.os.Environment
import androidx.compose.ui.unit.dp
import java.io.File

class ntufappInfo: Application() {
    companion object{
        const val tag = "NTUF"
        const val dTag = "NTUF_DEBUG"
        const val defaultTreeNum = 60
        const val dbhThreshold = 0.02
        const val htThreshold = 0.1
        const val changeDataQuota = 3
        const val outputDirName = "NTUEF_APP"
        private val documentsDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val outputDir = File(documentsDir, outputDirName)
    }
}