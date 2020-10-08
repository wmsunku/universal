package com.wms.base.sdk.system

import android.os.Environment
import com.wms.base.sdk.getApplication
import java.io.File

private const val LOCATION = "/file_data"

fun createFile(fileName: String): File {
    return mikFile(LOCATION, fileName)
}

fun createFile(location: String, fileName: String): File {
    return mikFile(location, fileName)
}

private fun getDirectory(location: String): File {
    val state = Environment.getExternalStorageState()
    val root = if (state == Environment.MEDIA_MOUNTED) {
        Environment.getExternalStorageDirectory()
    } else {
        getApplication().getExternalFilesDir(Environment.DIRECTORY_DCIM)
    }
    val directory = File(root!!.toString() + location)
    directory.mkdirs()
    return directory
}

private fun mikFile(location: String, fileName: String): File {
    return File(getDirectory(location), fileName)
}




