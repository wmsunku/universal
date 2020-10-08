package com.wms.bookreader.views.readview.manager

import com.wms.base.api.SpAPI
import com.wms.base.sdk.dpToPxInt
import com.wms.base.sdk.getScreenBrightness

class SettingManager {

    companion object {
        object VH {
            var instance = SettingManager()
        }

        fun Instance(): SettingManager {
            return VH.instance
        }

    }

    /**
     * 保存书籍阅读字体大小
     *
     * @param bookId     需根据bookId对应，避免由于字体大小引起的分页不准确
     * @param fontSizePx
     * @return
     */
    fun saveFontSize(bookId: String, fontSizePx: Int) {
        // 书籍对应
        SpAPI.Instance().putInt(getFontSizeKey(bookId), fontSizePx)
    }

    /**
     * 保存全局生效的阅读字体大小
     *
     * @param fontSizePx
     */
    fun saveFontSize(fontSizePx: Int) {
        saveFontSize("", fontSizePx)
    }

    fun getReadFontSize(bookId: String): Int {
        return SpAPI.Instance().getInt(getFontSizeKey(bookId), dpToPxInt(16f))
    }

    fun getReadFontSize(): Int {
        return getReadFontSize("")
    }

    private fun getFontSizeKey(bookId: String): String {
        return "$bookId-readFontSize"
    }

    fun getReadBrightness(): Int {
        return getScreenBrightness()
    }

    /**
     * 保存阅读界面屏幕亮度
     *
     * @param percent 亮度比例 0~100
     */
    fun saveReadBrightness(percent: Int) {
        var percent = percent
        if (percent > 100) {
//            RouterFactory.Instance().commonService.showTost("saveReadBrightnessErr CheckRefs")
            percent = 100
        }
        SpAPI.Instance().putInt(getLightnessKey(), percent)
    }

    private fun getLightnessKey(): String {
        return "readLightness"
    }

    fun saveReadProgress(bookId: String, currentChapter: Int, m_mbBufBeginPos: Int, m_mbBufEndPos: Int) {
        SpAPI.Instance()
                .putInt(getChapterKey(bookId), currentChapter)
                .putInt(getStartPosKey(bookId), m_mbBufBeginPos)
                .putInt(getEndPosKey(bookId), m_mbBufEndPos)
    }

    /**
     * 获取上次阅读章节及位置
     *
     * @param bookId
     * @return
     */
    fun getReadProgress(bookId: String): IntArray {
        val lastChapter = SpAPI.Instance().getInt(getChapterKey(bookId), 1)
        val startPos = SpAPI.Instance().getInt(getStartPosKey(bookId), 0)
        val endPos = SpAPI.Instance().getInt(getEndPosKey(bookId), 0)

        return intArrayOf(lastChapter, startPos, endPos)
    }

    fun removeReadProgress(bookId: String) {
        SpAPI.Instance()
                .remove(getChapterKey(bookId))
                .remove(getStartPosKey(bookId))
                .remove(getEndPosKey(bookId))
    }

    private fun getChapterKey(bookId: String): String {
        return "$bookId-chapter"
    }

    private fun getStartPosKey(bookId: String): String {
        return "$bookId-startPos"
    }

    private fun getEndPosKey(bookId: String): String {
        return "$bookId-endPos"
    }


//    fun addBookMark(bookId: String, mark: BookMark): Boolean {
//        var marks: MutableList<BookMark>? = SpAPI.Instance().getObject<ArrayList<BookMark>>(getBookMarksKey(bookId), ArrayList::class.java)
//        if (marks != null && marks.size > 0) {
//            for (item in marks) {
//                if (item.chapter === mark.chapter && item.startPos === mark.startPos) {
//                    return false
//                }
//            }
//        } else {
//            marks = ArrayList<BookMark>()
//        }
//        marks.add(mark)
//        SpAPI.Instance().putObject(getBookMarksKey(bookId), marks)
//        return true
//    }
//
//    fun getBookMarks(bookId: String): List<BookMark>? {
//        return SpAPI.Instance().getObject<ArrayList<BookMark>>(getBookMarksKey(bookId), ArrayList::class)
//    }

    fun clearBookMarks(bookId: String) {
        SpAPI.Instance().remove(getBookMarksKey(bookId))
    }

    private fun getBookMarksKey(bookId: String): String {
        return "$bookId-marks"
    }

    fun saveReadTheme(theme: Int) {
        SpAPI.Instance().putInt("readTheme", theme)
    }

    fun getReadTheme(): Int {
        return if (SpAPI.Instance().getBoolean("isNight", false)) {
            ThemeManager.NIGHT
        } else SpAPI.Instance().getInt("readTheme", 3)
    }

    /**
     * 是否可以使用音量键翻页
     *
     * @param enable
     */
    fun saveVolumeFlipEnable(enable: Boolean) {
        SpAPI.Instance().putBoolean("volumeFlip", enable)
    }

    fun isVolumeFlipEnable(): Boolean {
        return SpAPI.Instance().getBoolean("volumeFlip", true)
    }

    fun saveAutoBrightness(enable: Boolean) {
        SpAPI.Instance().putBoolean("autoBrightness", enable)
    }

    fun isAutoBrightness(): Boolean {
        return SpAPI.Instance().getBoolean("autoBrightness", false)
    }


    /**
     * 保存用户选择的性别
     *
     * @param sex male female
     */
    fun saveUserChooseSex(sex: String) {
        SpAPI.Instance().putString("userChooseSex", sex)
    }

    /**
     * 获取用户选择性别
     *
     * @return
     */
    fun getUserChooseSex(): String {
        return SpAPI.Instance().getString("userChooseSex", "male")
    }

    fun isUserChooseSex(): Boolean {
        return SpAPI.Instance().exists("userChooseSex")
    }

    fun isNoneCover(): Boolean {
        return SpAPI.Instance().getBoolean("isNoneCover", false)
    }

    fun saveNoneCover(isNoneCover: Boolean) {
        SpAPI.Instance().putBoolean("isNoneCover", isNoneCover)
    }
}