package com.example.ntufapp.utils

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.ntufapp.api.dataType.plotInfoResponse.PlotInfoResponse
import com.example.ntufapp.api.dataType.surveyDataForUpload.SurveyDataForUpload
import com.example.ntufapp.api.getTodayDate
import com.example.ntufapp.api.transformPlotDataToSurveyDataForUpload
import com.example.ntufapp.api.transformPlotInfoResponseToPlotData
import com.example.ntufapp.data.ntufappInfo.Companion.outputDir
import com.example.ntufapp.model.PlotData
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun parseJsonToPlotData(uri: Uri, context: Context): PlotData? {
    val inputStream = context.contentResolver.openInputStream(uri)
    val jsonString = inputStream?.bufferedReader()?.use(BufferedReader::readText)

    try {
        val plotInfoResponse = Gson().fromJson(jsonString, PlotInfoResponse::class.java)
        return transformPlotInfoResponseToPlotData(plotInfoResponse, context)
    } catch (e: Exception) {
        // Handle parsing errors here
        return null
    } finally {
        // Close the InputStream after use
        inputStream?.close()
    }
}

fun parseJsonToSurveyDataForUpload(uri: Uri, context: Context): SurveyDataForUpload? {
    val inputStream = context.contentResolver.openInputStream(uri)
    val jsonString = inputStream?.bufferedReader()?.use(BufferedReader::readText)

    try {
        val surveyDataForUpload = Gson().fromJson(jsonString, SurveyDataForUpload::class.java)
        return surveyDataForUpload
    } catch (e: Exception) {
        // Handle parsing errors here
        return null
    } finally {
        // Close the InputStream after use
        inputStream?.close()
    }
}

fun getFilenameWithFormat(plotData: PlotData, filename: String = "", toCSV: Boolean = false): String {
    if (filename.isNotEmpty()) {
        if (toCSV) {
            return filename.replaceAfter(".", "csv")
        } else {
            return filename
        }
    } else {
        val formattedFilename = plotData.ManageUnit + plotData.PlotName + "_" + plotData.PlotNum
        if (toCSV) {
            return "$formattedFilename.csv"
        } else {
            return "$formattedFilename.json"
        }
    }
}

fun checkIfFileExists(context: Context, filename: String): Boolean {
    if (!outputDir.exists()) {
        if(!outputDir.mkdir()) {
            showMessage(context, "儲存失敗：無法建立資料夾！")
        }
    }

    val file = File(outputDir, filename)
    return file.exists()
}

@RequiresApi(Build.VERSION_CODES.R)
fun saveFile(context: Context, plotData: PlotData, outputDir: File, validFilename: String, toCSV: Boolean = false) {
    // Create the file path
    val file = File(outputDir, validFilename)

    if (!Environment.isExternalStorageManager()) {
        showMessage(context, "請先取得權限！")
        val permissionIntent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        context.startActivity(permissionIntent)
    } else {
        try {
            if (toCSV) {
                val flattenedPlotData = flattenPlotData(plotData)
                writeToCSV(file, flattenedPlotData)
            } else {
                // Read the Json data
                val uploadPlotData = transformPlotDataToSurveyDataForUpload(plotData)
                val gson = Gson()
                val myJson = gson.toJson(uploadPlotData)
                writeToJson(context, file, myJson)
            }
            showMessage(context, "檔案${file.absoluteFile.name}儲存成功！\n${file.absoluteFile}")
        } catch (e: IOException) {
            showMessage(context, "儲存失敗：${e.message}")
            e.printStackTrace()
        }
    }
}

fun writeToCSV(outputFile: File, flattenedPlotData: List<List<String>>) {

    if (!outputFile.exists()) {
        outputFile.createNewFile()
        Log.d("saveFile", "outputFile does not exist")
    } else {
        outputFile.delete()
        Log.d("saveFile", "outputFile exists")
    }

    val fileOutputStream = FileOutputStream(outputFile)
    val bufferedWriter = BufferedWriter(fileOutputStream.writer())

    flattenedPlotData.forEach { row ->
        bufferedWriter.write(row.joinToString(","))
        bufferedWriter.newLine()
    }
    Log.d("saveFile", "outputFile: ${outputFile.absolutePath}")

    bufferedWriter.close()
    fileOutputStream.close()
}

fun writeToJson(context: Context, outputFile: File, jsonString: String) {
    val fileOutputStream = FileOutputStream(outputFile)
    fileOutputStream.write(jsonString.toByteArray())
    notifyMediaScanner(context, outputFile)
    fileOutputStream.close()
}

fun notifyMediaScanner(context: Context, file: File) {
    MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)
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

fun generateUniqueFilename(context: Context, baseFilename: String, format: String = ".json"): String {
    var version = 0
    var uniqueFilename = baseFilename + format
    Log.d("saveFile", "unique: $uniqueFilename")

    while (checkIfFileExists(context, uniqueFilename)) {
        version++
        uniqueFilename = baseFilename + "_$version" + format
    }
    return uniqueFilename
}