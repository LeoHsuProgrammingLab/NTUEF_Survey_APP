package com.example.ntufapp.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.ntufapp.data.ntufappInfo.Companion.outputDirName
import com.example.ntufapp.model.PlotData
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun maxThree(a: Double, b: Double, c: Double): Double {
    return maxOf(a, maxOf(b, c))
}

fun minThree(a: Double, b: Double, c: Double): Double {
    return minOf(a, minOf(b, c))
}

fun showMessage(context: Context, s: String) {
    Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
}

fun parseJsonToMetaData(uri: Uri, context: Context): PlotData? {
    val inputStream = context.contentResolver.openInputStream(uri)
    val jsonString = inputStream?.bufferedReader()?.use(BufferedReader::readText)

    return try {
        Gson().fromJson(jsonString, PlotData::class.java)
    } catch (e: Exception) {
        // Handle parsing errors here
        null
    } finally {
        // Close the InputStream after use
        inputStream?.close()
    }
}

fun saveJsonFile(plotData: PlotData, fileName: String, context: Context) {
    // Research
//    val permission = checkPermission(context)
//    if (!permission) {
//        // ask for permission
//
//
//        showMessage(context, "儲存失敗：請先開啟儲存權限！")
//        return
//    } else {
//        Log.d("saveJsonFile", "permission granted")
//    }

    // Check if the string is valid
    var validFileName: String
    if (fileName.isNotEmpty()) {
        validFileName = "$fileName.json"
    } else {
        showMessage(context, "儲存失敗：請輸入檔名！")
        return
    }

    val dirName = outputDirName
    // Read the data
    val gson = Gson()
    val myJson = gson.toJson(plotData)

    val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    // The below is the app-specific directory, and it may be invisible to the user.
    // val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

    val appDir = File(documentsDir, dirName)
    if (!appDir.exists()) {
        Log.d("saveJsonFile", "appDir does not exist")
        if(!appDir.mkdir()) {
            Log.d("saveJsonFile", "mkdir failed")
        }
    }
    val file = File(appDir, validFileName)
    Log.d("saveJsonFile", "file: ${file.absoluteFile} ")

    try {
        writeToJson(context, file, myJson)
        showMessage(context, "儲存成功！${file.absoluteFile}")
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun getFileName(context: Context, uri: Uri?): String {
    if (uri == null) {
        return ""
    }

    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex != -1) {
            return it.getString(nameIndex)
        }
    }
    return ""
}

// Research
fun checkPermission(context: Context): Boolean {
    val permission = context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    return permission == android.content.pm.PackageManager.PERMISSION_GRANTED
}

fun writeToJson(context: Context, outputFile: File, jsonString: String) {
    val fileOutputStream = FileOutputStream(outputFile)
    fileOutputStream.write(jsonString.toByteArray())
    fileOutputStream.close()
    Log.d("writeToJson", "file saved")
}