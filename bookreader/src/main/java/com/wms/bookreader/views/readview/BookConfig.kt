package com.wms.bookreader.views.readview

class BookConfig {

    companion object {
        const val DECIMAL_FORMAT = "#0.00"
        const val TIME_FORMAT = "HH:mm"


        const val NO_PRE_PAGE = 0x00
        const val NO_NEXT_PAGE = 0x11

        const val PRE_CHAPTER_LOAD_FAILURE = 0x22
        const val NEXT_CHAPTER_LOAD_FAILURE = 0x33

        const val LOAD_SUCCESS = 0x44
    }
}