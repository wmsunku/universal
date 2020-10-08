package com.wms.base.sdk

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.*
import java.net.URL
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.R.attr.bitmap
import android.content.Context


private const val IMAGE_PATH = "image_path"

fun getBitmapFormUri(ac: Activity, uri: Uri): Bitmap? {
    var input = ac.contentResolver.openInputStream(uri)
    val onlyBoundsOptions = BitmapFactory.Options()
    onlyBoundsOptions.inJustDecodeBounds = true
    onlyBoundsOptions.inDither = true
    onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
    BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
    input!!.close()
    val originalWidth = onlyBoundsOptions.outWidth
    val originalHeight = onlyBoundsOptions.outHeight
    if (originalWidth == -1 || originalHeight == -1) {
        return null
    }
    val hh = 800f
    val ww = 480f
    var be = 1
    if (originalWidth > originalHeight && originalWidth > ww) {
        be = (originalWidth / ww).toInt()
    } else if (originalWidth < originalHeight && originalHeight > hh) {
        be = (originalHeight / hh).toInt()
    }
    if (be <= 0)
        be = 1
    val bitmapOptions = BitmapFactory.Options()
    bitmapOptions.inSampleSize = be
    bitmapOptions.inDither = true
    bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
    input = ac.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
    input!!.close()

    return compressImage(bitmap)
}

fun getImagePath(act: Activity, uri: Uri): String {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = act.contentResolver.query(uri, filePathColumn, null, null, null)
    cursor!!.moveToFirst()
    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
    val imagePath = cursor.getString(columnIndex)
    cursor.close()
    println(IMAGE_PATH + imagePath)
    return imagePath
}

fun rotateBitmapByDegree(bm: Bitmap, degree: Int): Bitmap {
    var returnBm: Bitmap? = null

    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    try {
        returnBm = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
    }

    if (returnBm == null) {
        returnBm = bm
    }
    if (bm != returnBm) {
        bm.recycle()
    }
    return returnBm
}

fun getBitmapDegree(path: String): Int {
    var degree = 0
    try {
        val exifInterface = ExifInterface(path)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return degree
}

fun getLocalOrNetBitmap(url: String): Bitmap? {
    var inStream: BufferedInputStream? = null
    var outStream: BufferedOutputStream? = null
    var dataStream: ByteArrayOutputStream? = null
    try {
        inStream = BufferedInputStream(URL(url).openStream(), 2 * 1024)
        dataStream = ByteArrayOutputStream()
        outStream = BufferedOutputStream(dataStream, 2 * 1024)
        copy(inStream, outStream)
        val data: ByteArray? = dataStream.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
        return compressImage(bitmap)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        if (inStream != null) {
            inStream.close()
        }
        if (outStream != null) {
            outStream.flush()
            outStream.close()
        }
        if (dataStream != null) {
            dataStream.flush()
            dataStream.close()
        }
    }
    return null
}

// 按质量压缩
fun compressImage(image: Bitmap): Bitmap {
    val baos = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    var options = 90
    while (baos.toByteArray().size / 1024 > 100) {
        baos.reset()
        image.compress(Bitmap.CompressFormat.JPEG, options, baos)
        options -= 10
    }
    val isBm = ByteArrayInputStream(baos.toByteArray())
    return BitmapFactory.decodeStream(isBm, null, null)
}

//按大小压缩
fun compressScale(image: Bitmap): Bitmap {
    val baos = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    if (baos.toByteArray().size / 1024 > 1024) {
        baos.reset()
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos)
    }
    val newOpts = BitmapFactory.Options()
    newOpts.inJustDecodeBounds = true
    val w = newOpts.outWidth
    val h = newOpts.outHeight
    val hh = 512f
    val ww = 512f
    var be = 1
    if (w > h && w > ww) {
        be = (newOpts.outWidth / ww).toInt()
    } else if (w < h && h > hh) {
        be = (newOpts.outHeight / hh).toInt()
    }
    if (be <= 0)
        be = 1
    newOpts.inSampleSize = be
    val isBm = ByteArrayInputStream(baos.toByteArray())
    val bitmap = BitmapFactory.decodeStream(isBm, null, newOpts)
    return compressImage(bitmap)
}

@Throws(IOException::class)
fun copy(source: InputStream, sink: OutputStream): Long {
    var read = 0L
    val buf = ByteArray(8192)
    var n: Int
    while (isGo(source, buf)) {
        n = source.read(buf)
        sink.write(buf, 0, n)
        read += n.toLong()
    }
    return read
}

private fun isGo(source: InputStream, buf: ByteArray): Boolean {
    if (source.read(buf) > 0) {
        return true
    }
    return false
}


fun resizeImage(source: Bitmap?, dstWidth: Int, dstHeight: Int): Bitmap? {
    if (source == null) {
        return null
    }
    return if (source.width === dstWidth && source.height === dstHeight) {
        source
    } else Bitmap.createScaledBitmap(source, dstWidth, dstHeight, true)

}

fun bitmap2Drawable(bitmap: Bitmap): Drawable {
    return BitmapDrawable(getResources(), bitmap)
}
