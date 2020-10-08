package com.wms.bookreader.reader.ui.activity

import android.view.View
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.createView
import com.wms.bookreader.R

class BookDetailActivity: BaseActivity() {

    override fun getLayout(): View {
        return createView(R.layout.activity_book_detail)
    }

    override fun initView() {
    }

    override fun slidEnable(): Boolean {
        return true
    }

}