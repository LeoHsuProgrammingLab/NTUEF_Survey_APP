package com.example.ntufapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

const val REQUEST_CODE_PICK_DIR = 1

@Composable
// In your Content composable, call ExternalStoragePermissionHandler with your content when permission is granted.
fun ExternalStoragePermissionHandler(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current as ComponentActivity
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onPermissionGranted()
        }
    }

    DisposableEffect(context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                onPermissionGranted()
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                launcher.launch(intent)
            }
        } else {
            showMessage(context, "請使用android 11以上版本")
        }

        onDispose { }
    }
}

fun checkPermission(context: Context): Boolean {
    val permission = context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    return permission == android.content.pm.PackageManager.PERMISSION_GRANTED
}


