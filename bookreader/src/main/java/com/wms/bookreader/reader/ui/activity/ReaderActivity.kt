package com.wms.bookreader.reader.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.constact.OnApplyPermissionsNextListener
import com.wms.base.sdk.createView
import com.wms.base.sdk.system.checkPermission
import com.wms.base.sdk.system.hideState
import com.wms.bookreader.R
import com.wms.bookreader.sdk.getFlipStyle
import com.wms.bookreader.sdk.isNight
import com.wms.bookreader.views.readview.*
import com.wms.bookreader.views.readview.bean.BookMixAToc
import com.wms.bookreader.views.readview.manager.ThemeManager
import kotlinx.android.synthetic.main.activity_reader.*
import java.util.ArrayList

class ReaderActivity: BaseActivity(), OnReadStateChangeListener {

    private val mChapterList = ArrayList<BookMixAToc.MixToc.Chapters>()
    private var mPageWidget: BaseReadView? = null

    companion object {
        fun start(ac: Activity) {
            val intent = Intent(ac, ReaderActivity::class.java)
            ac.startActivity(intent)
        }
    }

    override fun beforeCreate(savedInstanceState: Bundle?) {
        hideState()
    }

    override fun getLayout(): View {
        return createView(R.layout.activity_reader)
    }

    override fun initView() {

        val bookId = "book_0001"
        val chapter = BookMixAToc.MixToc.Chapters()
        chapter.title = "《女子无殇》"
        mChapterList.add(chapter)

        findViewById<View>(R.id.toolBar).visibility = View.GONE
        bottomMenu.visibility = View.GONE


        val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        checkPermission(this, permissions, object : OnApplyPermissionsNextListener{
            override fun nextAction() {
                loadNewBook(bookId)
            }
        })
    }

    fun loadNewBook(bookId: String) {
        when (getFlipStyle()) {
            0 -> mPageWidget = PageWidget(this, bookId, mChapterList, this)
            1 -> mPageWidget = OverlappedWidget(this, bookId, mChapterList, this)
            2 -> mPageWidget = NoAimWidget(this, bookId, mChapterList, this)
        }
        if (isNight()) {
            mPageWidget!!.setTextColor(ContextCompat.getColor(this, R.color.chapter_content_night),
                    ContextCompat.getColor(this, R.color.chapter_title_night))
        }
        bookLayout.removeAllViews()
        bookLayout.addView(mPageWidget)

        if (!mPageWidget!!.isPrepared) {
            mPageWidget!!.init(ThemeManager.YELLOW)
        } else {
            mPageWidget!!.jumpToChapter(1)
        }
        mPageWidget!!.jumpToChapter(1)
    }

    override fun onChapterChanged(chapter: Int) {

    }

    override fun onPageChanged(chapter: Int, page: Int) {

    }

    override fun onLoadChapterFailure(chapter: Int) {

    }

    override fun onCenterClick() {

    }

    override fun onFlip() {

    }




}