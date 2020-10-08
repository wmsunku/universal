package com.wms.bookreader.sdk

import com.wms.base.api.SpAPI
import com.wms.base.sdk.system.createFile
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.Charset

const val FLIP_STYLE = "flip_style"
const val IS_NIGHT = "is_night"


fun getFlipStyle(): Int {
    return SpAPI.Instance().getInt(FLIP_STYLE, 0)
}

fun isNight(): Boolean {
    return SpAPI.Instance().getBoolean(IS_NIGHT, true)
}


fun getChapterPath(bookId: String, chapter: Int): String {
    return bookId + File.separator + chapter + ".txt"
}

fun getChapterFile(bookId: String, chapter: Int): File {
//            val file = File(getChapterPath(bookId, chapter))
    val file = createFile("/Tencent/QQfile_recv", "/《女子无殇》.txt")
    if (!file.exists())
        file.mkdirs()
    return file
}

fun getChapterFile(path: String): File {
//            val file = File(getChapterPath(bookId, chapter))
    val file = createFile("/Tencent/QQfile_recv", "/《女子无殇》.txt")
    if (!file.exists())
        file.mkdirs()
    return file
}

fun getCharset(fileName: String): Charset? {
    var bis: BufferedInputStream? = null
    var charset = Charset.forName("GBK").newEncoder().charset()
    val first3Bytes = ByteArray(3)
    try {
        var checked = false
        bis = BufferedInputStream(FileInputStream(fileName))
        bis.mark(0)
        var read = bis.read(first3Bytes, 0, 3)
        if (read == -1)
            charset = Charsets.UTF_8
        if (first3Bytes[0] == 0xFF.toByte() && first3Bytes[1] == 0xFE.toByte()) {
            charset = Charsets.UTF_16LE
            checked = true
        } else if (first3Bytes[0] == 0xFE.toByte() && first3Bytes[1] == 0xFF.toByte()) {
            charset = Charsets.UTF_16BE
            checked = true
        } else if (first3Bytes[0] == 0xEF.toByte()
                && first3Bytes[1] == 0xBB.toByte()
                && first3Bytes[2] == 0xBF.toByte()) {
            charset = Charsets.UTF_8
            checked = true
        }
        bis.mark(0)
        if (!checked) {

            while (bis.read() != -1) {
                read = bis.read()
                if (read >= 0xF0)
                    break
                if (0x80 <= read && read <= 0xBF)
                // 单独出现BF以下的，也算是GBK
                    break
                if (0xC0 <= read && read <= 0xDF) {
                    read = bis.read()
                    if (0x80 <= read && read <= 0xBF)
                    // 双字节 (0xC0 - 0xDF)
                    // (0x80 - 0xBF),也可能在GB编码内
                        continue
                    else
                        break
                } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                    read = bis.read()
                    if (0x80 <= read && read <= 0xBF) {
                        read = bis.read()
                        if (0x80 <= read && read <= 0xBF) {
                            charset = Charsets.UTF_8
                            break
                        } else
                            break
                    } else
                        break
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (bis != null) {
            try {
                bis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    return charset
}


