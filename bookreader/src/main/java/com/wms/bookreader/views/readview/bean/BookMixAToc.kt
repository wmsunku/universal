package com.wms.bookreader.views.readview.bean

import java.io.Serializable

class BookMixAToc : Base() {

    /**
     * _id:577e528e2160421a02d7380d
     * name:优质书源
     * link:http://vip.zhuishushenqi.com/toc/577e528e2160421a02d7380d
     */
    var mixToc: MixToc? = null

    class MixToc : Serializable {
        var _id: String? = null
        var book: String? = null
        var chaptersUpdated: String? = null
        /**
         * title : 第一章 死在万花丛中
         * link : http://vip.zhuishushenqi.com/chapter/577e5290260289ff64a29213?cv=1467896464908
         * id : 577e5290260289ff64a29213
         * currency : 15
         * unreadble : false
         * isVip : false
         */

        var chapters: List<Chapters>? = null

        class Chapters : Serializable {
            var title: String? = null
            var link: String? = null
            var id: String? = null
            var currency: Int = 0
            var unreadble: Boolean = false
            var isVip: Boolean = false

            constructor() {}

            constructor(title: String, link: String) {
                this.title = title
                this.link = link
            }
        }
    }

}
