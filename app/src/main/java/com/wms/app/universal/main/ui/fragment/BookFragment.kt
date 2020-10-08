package com.wms.app.universal.main.ui.fragment

import com.wms.app.universal.R
import com.wms.base.common.BaseFragment
import com.wms.base.jni.ImageNameJNI
import com.wms.bookreader.reader.ui.activity.ReaderActivity
import kotlinx.android.synthetic.main.fragment_book.*

class BookFragment: BaseFragment() {

    override fun getLayout(): Int {
        return R.layout.fragment_book
    }

    override fun initView() {
        book.setOnClickListener {
            ReaderActivity.start(activity!!)
        }
    }
}