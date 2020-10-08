package com.wms.bookreader.views.readview.bean

import java.io.Serializable

class BookMark: Serializable {

    var chapter: Int = 0

    var title: String? = null

    var startPos: Int = 0

    var endPos: Int = 0

    var desc = ""

}