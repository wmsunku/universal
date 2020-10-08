package com.wms.media.music.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.util.LruCache
import android.text.TextUtils
import com.wms.base.sdk.getResources
import com.wms.base.sdk.getScreenWidth
import com.wms.base.sdk.resizeImage
import com.wms.media.R
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.HashMap

class CoverLoader {

    private val THUMBNAIL_MAX_LENGTH = 500
    private val KEY_NULL = "null"

    private var context: Context? = null
    private var cacheMap: MutableMap<Int, LruCache<String, Bitmap>>? = null
    private var roundLength = getScreenWidth() / 2


    companion object {
        val THUMB = 5001
        val ROUND = 5002
        val BLUR = 5003

        private object VH {
            val instance = CoverLoader()
        }

        fun get(): CoverLoader {
            return VH.instance
        }
    }


    fun init(context: Context) {
        this.context = context.applicationContext

        // 获取当前进程的可用内存（单位KB）
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        // 缓存大小为当前进程可用内存的1/8
        val cacheSize = maxMemory / 8
        val thumbCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    bitmap.allocationByteCount / 1024
                } else {
                    bitmap.byteCount / 1024
                }
            }
        }
        val roundCache = LruCache<String, Bitmap>(10)
        val blurCache = LruCache<String, Bitmap>(10)

        cacheMap = HashMap(3)
        cacheMap!![THUMB] = thumbCache
        cacheMap!![ROUND] = roundCache
        cacheMap!![BLUR] = blurCache
    }

    fun setRoundLength(roundLength: Int) {
        if (this.roundLength != roundLength) {
            this.roundLength = roundLength
            cacheMap!![ROUND]!!.evictAll()
        }
    }

//    fun loadThumb(path: String): Bitmap {
//        return loadCover(path, THUMB)
//    }
//
//    fun loadRound(path: String): Bitmap {
//        return loadCover(path, ROUND)
//    }
//
//    fun loadBlur(path: String): Bitmap {
//        return loadCover(path, BLUR)
//    }

//    private fun loadCover(path: String?, type: Int): Bitmap {
//        var bitmap: Bitmap?
//        val cache = cacheMap!![type]
//        if (TextUtils.isEmpty(path)) {
//            bitmap = cache!!.get(KEY_NULL)
//            if (bitmap != null) {
//                return bitmap
//            }
//
//            bitmap = getDefaultCover(type)
//            cache!!.put(KEY_NULL, bitmap)
//            return bitmap
//        }
//
//        bitmap = cache!!.get(path!!)
//        if (bitmap != null) {
//            return bitmap
//        }
//        bitmap = loadCoverByType(path, type)
//        if (bitmap != null) {
//            cache.put(path, bitmap)
//            return bitmap
//        }
//
//        return loadCover(null, type)
//    }

    fun getDefaultCover(type: Int): Bitmap {
        return when (type) {
            ROUND -> {
                var bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_page_default_cover)
                bitmap = resizeImage(bitmap, roundLength, roundLength)
                bitmap
            }
            BLUR -> BitmapFactory.decodeResource(getResources(), R.drawable.play_page_default_bg)
            else -> BitmapFactory.decodeResource(getResources(), R.drawable.default_cover)
        }
    }




}