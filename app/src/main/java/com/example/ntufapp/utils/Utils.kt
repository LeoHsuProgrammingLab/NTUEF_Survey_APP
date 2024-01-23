package com.example.ntufapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.ntufapp.data.DataSource.columnName
import com.example.ntufapp.data.ntufappInfo.Companion.outputDir
import com.example.ntufapp.data.ntufappInfo.Companion.outputDirName
import com.example.ntufapp.model.PlotData
import com.example.ntufapp.ui.widget.GeneralConfirmDialog
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
                val gson = Gson()
                val myJson = gson.toJson(plotData)
                writeToJson(file, myJson)
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

fun generateUniqueFilename(context: Context, baseFilename: String, format: String = ".json"): String {
    var version = 0
    var uniqueFilename = baseFilename + format
    while (checkIfFileExists(context, uniqueFilename)) {
        version++
        uniqueFilename = baseFilename + "_$version" + format
    }
    return uniqueFilename
}

// Research
fun checkPermission(context: Context): Boolean {
    val permission = context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    return permission == android.content.pm.PackageManager.PERMISSION_GRANTED
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