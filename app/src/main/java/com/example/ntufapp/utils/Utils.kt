package com.example.ntufapp.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.ntufapp.data.DataSource.columnName
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


fun saveFile(context: Context, plotData: PlotData, filename: String = "", toCSV: Boolean) {
    // Read the data
    val gson = Gson()
    val myJson = gson.toJson(plotData)

    // Check if the string is valid
    var validFilename: String
    if (filename.isNotEmpty()) {
        validFilename = if (toCSV) {
            filename.replaceAfter(".", "csv")
        } else {
            filename
        }
    } else {
        val formattedFilename = plotData.ManageUnit + plotData.PlotName + "_" + plotData.PlotNum
        validFilename = if (toCSV) {
            "$formattedFilename.csv"
        } else {
            "$formattedFilename.json"
        }
    }

    val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val dirName = outputDirName
    // The below is the app-specific directory, and it may be invisible to the user.
    // val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

    // Create the directory if it doesn't exist
    val appDir = File(documentsDir, dirName)
    if (!appDir.exists()) {
        if(!appDir.mkdir()) {
            showMessage(context, "儲存失敗：無法建立資料夾！")
        }
    }
    // Create the file path
    var file = File(appDir, validFilename)
    var count = 1
    while (file.exists()) {
        val postfixStart = validFilename.lastIndexOf(".")
        file = File(appDir, "${validFilename.substring(0, postfixStart)}_${count}${validFilename.substring(postfixStart)}")
        count++
    }

    try {
        if (toCSV) {
            val flattenedPlotData = flattenPlotData(plotData)
            Log.d("saveFile", "flattenedPlotData: $flattenedPlotData")
            writeToCSV(file, flattenedPlotData)
        } else {
            writeToJson(file, myJson)
        }
        showMessage(context, "儲存成功！${file.absoluteFile}")
    } catch (e: IOException) {
        showMessage(context, "儲存失敗：${e.message}")
        e.printStackTrace()
    }
}

fun flattenPlotData(plotData: PlotData): List<List<String>> {
    val flattenedPlotData = mutableListOf<List<String>>()

    // Add the header
    val header = mutableListOf<String>()
    columnName.forEach {
        header.add(it)
    }
    flattenedPlotData.add(header)

    // Add the data
    plotData.PlotTrees.forEach { tree ->
        val row = listOf(
            tree.SampleNum.toString(),
            tree.Species,
            tree.DBH.toString(),
            tree.State.joinToString(";"),
            tree.MeasHeight.toString(),
            tree.VisHeight.toString(),
            tree.ForkHeight.toString(),
            plotData.Date,
            plotData.ManageUnit,
            plotData.SubUnit,
            plotData.PlotNum,
            plotData.PlotName,
            plotData.PlotArea.toString(),
            plotData.PlotType,
            plotData.TWD97_X,
            plotData.TWD97_Y,
            plotData.Altitude.toString(),
            plotData.Slope.toString(),
            plotData.Aspect,
            plotData.Surveyor.joinToString(";"),
            plotData.HtSurveyor.joinToString(";"),
        )

        flattenedPlotData.add(row)
    }
    return flattenedPlotData
}

fun writeToCSV(outputFile: File, flattenedPlotData: List<List<String>>) {

    if (!outputFile.exists()) {
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

fun writeToJson(outputFile: File, jsonString: String) {
    val fileOutputStream = FileOutputStream(outputFile)
    fileOutputStream.write(jsonString.toByteArray())
    fileOutputStream.close()
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