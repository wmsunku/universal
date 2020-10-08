package com.wms.base.sdk

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.wms.base.api.DataAPI
import com.wms.base.sdk.constact.OnApplyPermissionsNextListener
import com.wms.base.sdk.constact.OnCallBackPhotoListener
import com.wms.base.sdk.constant.SystemConfig
import com.wms.base.sdk.system.*

private var callBackPhotoListener: OnCallBackPhotoListener? = null
private var isCut: Boolean = false

fun selectPhoto(code: Int, bool: Boolean, listener: OnCallBackPhotoListener?) {
    selectPhoto(AcManager.Instance().getCurrActivity(), code, bool, listener)
}

fun selectPhoto(ac: Activity, code :Int, bool: Boolean, listener: OnCallBackPhotoListener?){
    isCut = bool
    callBackPhotoListener = listener
    when(code) {
        SystemConfig.CAMERA_CODE -> handlerCamera(ac)
        SystemConfig.ROLL_CODE -> handlerAlbum(ac)
    }
}

private fun handlerCamera(ac: Activity) {
    val permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    checkPermission(ac, permissions, object : OnApplyPermissionsNextListener {
        override fun nextAction() {
            callCamera(ac, SystemConfig.CAMERA_CODE)
        }
    })
}

private fun handlerAlbum(ac: Activity) {
    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    checkPermission(ac, permissions, object : OnApplyPermissionsNextListener {
        override fun nextAction() {
            callAlbum(ac, SystemConfig.ROLL_CODE)
        }
    })

}

fun handlerPhotoResult(ac :Activity, requestCode :Int, data : Intent?){
    when (requestCode) {
        SystemConfig.CAMERA_CODE -> handlerPhoto(ac, getPhotoUri()!!)
        SystemConfig.ROLL_CODE -> handlerPhoto(ac, data!!.data)
        SystemConfig.CUT_CODE -> handlerCut(ac, data!!)
    }
}

private fun handlerCut(ac: Activity, data: Intent) {
    var photoBmp: Bitmap? = null
    try {
        photoBmp = if (DataAPI.isMoreN()) {
            val uri = Uri.parse(data.action)
            getBitmapFormUri(ac, uri)
        } else {
            data.getParcelableExtra("data")
        }
    }catch(e :Exception){
        e.printStackTrace()
    }
    resultData(photoBmp)
}

private fun handlerPhoto(ac: Activity, uri: Uri) {
    if(isCut) {
        callCut(ac, uri, SystemConfig.CUT_CODE)
        return
    }
    val file = getPhotoFile()
    var path: String? = null
    if(!DataAPI.hasNull(file)) {
        path = file!!.absolutePath
    }
    val bitmap = getBitmapFormUri(ac, uri)
    if(!DataAPI.hasNull(path, bitmap)) {
        resultData(getBitmap(path!!, bitmap!!)!!)
        return
    }
    resultData(null)
}

private fun getBitmap(path: String, bitmap: Bitmap): Bitmap? {
    val degree = getBitmapDegree(path)
    return rotateBitmapByDegree(bitmap, degree)
}

private fun resultData(bitmap: Bitmap?) {
    if(DataAPI.hasNull(bitmap)) {
        return
    }
    if(callBackPhotoListener != null) {
        callBackPhotoListener!!.resultBitmap(bitmap)
    }
}
