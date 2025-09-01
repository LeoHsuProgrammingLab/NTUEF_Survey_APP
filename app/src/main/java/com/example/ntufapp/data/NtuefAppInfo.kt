package com.example.ntufapp.data

import android.app.Application
import android.os.Environment
import java.io.File

class NtufAppInfo: Application() {
    companion object{
        const val dbhThreshold = 0.0
        const val htThreshold = 0.0
        const val outputDirName = "NTUEF_APP"
        private val documentsDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val outputDir = File(documentsDir, outputDirName)
    }
}