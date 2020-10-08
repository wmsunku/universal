package com.wms.base.sdk.system

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.wms.base.api.AppAPI
import com.wms.base.api.DataAPI
import java.io.File

private var photoFile: File? = null
private var photoURI: Uri? = null


fun createPhotoFile(): File {
    val fileName = "TEXT_" + System.currentTimeMillis() + ".jpg"
    return createFile(fileName)
}

fun getPhotoUri(): Uri? {
    return photoURI
}

fun getPhotoFile(): File? {
    return photoFile
}

private fun getUri(act: Activity, photoFile: File?): Uri? {
    var photoURI: Uri? = null
    try {
        if (DataAPI.hasNull(photoFile)) {
            return null
        }
        photoURI = if (DataAPI.isMoreN()) {
            FileProvider.getUriForFile(act, AppAPI.getAppProcessName(act), photoFile!!)
        } else {
            Uri.fromFile(photoFile)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return photoURI
}

//拍照
@Throws(Exception::class)
fun callCamera(act: Activity, CODE: Int) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (DataAPI.isMoreN()) {
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    }
    if (intent.resolveActivity(act.packageManager) != null) {
        photoFile = createPhotoFile()
        photoURI = getUri(act, photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        act.startActivityForResult(intent, CODE)
    }
}

// 相册
fun callAlbum(act: Activity, CODE: Int) {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    act.startActivityForResult(intent, CODE)
}

// 裁剪
@Throws(Exception::class)
fun callCut(act: Activity, uri: Uri, CODE: Int) {
    val intent = Intent("com.android.camera.action.CROP")
    intent.setDataAndType(uri, "image/*")

    if (DataAPI.isMoreN()) {
        intent.putExtra("return-data", false)
        intent.putExtra("noFaceDetection", false)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    } else {
        intent.putExtra("return-data", true)
    }
    intent.putExtra("crop", "true")


    intent.putExtra("aspectX", 1)
    intent.putExtra("aspectY", 1)
    intent.putExtra("outputX", 300)
    intent.putExtra("outputY", 300)
    intent.putExtra("scale", true)

    //将剪切的图片保存到目标Uri中
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createPhotoFile()))
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
    intent.putExtra("noFaceDetection", true)
    act.startActivityForResult(intent, CODE)
}