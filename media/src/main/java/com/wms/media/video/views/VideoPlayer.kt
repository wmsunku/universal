package com.wms.media.video.views

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.Toast
import com.wms.base.sdk.createView
import com.wms.media.R
import com.wms.media.sdk.*
import org.greenrobot.eventbus.EventBus

class VideoPlayer : FrameLayout, OnVideoPlayerListener,
        OnBindPlayerListener, OnCountTimeListener, OnVideoLifeListener {

    private var isPlaying = false
    private var hasPlaying = false
    private var viewHolder: VideoViewHolder? = null
    private var formView: ViewGroup? = null
    private var toView: ViewGroup? = null
    private var isFull = false
    private var mTextureView: VideoTextureView? = null
    private var bufferProgress = 0
    private var mSurfaceTexture: SurfaceTexture? = null
    private var taskNum = getVideoTaskNum()

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, ttrs: AttributeSet) : super(context, ttrs) {
        init(context)
    }

    private fun init(context: Context) {
        val contentView = createView(context, R.layout.layout_video_player)
        this.addView(contentView)
        initView(contentView)
    }

    private fun initView(view: View) {
        viewHolder = VideoViewHolder(view)

        if (mTextureView == null) {
            mTextureView = VideoTextureView(context)
        }
        mTextureView!!.keepScreenOn = true
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER)

        getViewHolder().viewPlayer.removeAllViews()
        getViewHolder().viewPlayer.addView(mTextureView, 0, params)
        initListener(getViewHolder())
    }

    private fun initListener(vh: VideoViewHolder) {
        VideoManager.newInstance().init(taskNum,this)
        vh.ivPlay.setOnClickListener {
            if (!isPlaying) {
                start()
            }
        }

        vh.viewPlayer.setOnClickListener {
            if (isPlaying) {
                pause()
            }
        }

        vh.ivEnter.setOnClickListener {
            if (isFull) {
                exitFull()
            } else {
                enterFull()
            }
        }
        vh.sbProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                vh.tvCurrentTime.text = getTime(progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                hasPlaying = isPlaying
                pause()
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                val progress = seek.progress
                vh.sbProgress.progress = progress
                VideoManager.newInstance().seekProgress(progress)
                if (hasPlaying) {
                    start()
                }
            }
        })

        mTextureView!!.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return mSurfaceTexture == null
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                if (mSurfaceTexture == null) {
                    mSurfaceTexture = surface
                } else {
                    mTextureView!!.surfaceTexture = mSurfaceTexture
                }
            }

        }

    }

    fun bindView(v1: ViewGroup, v2: ViewGroup) {
        formView = v1
        toView = v2

    }

    /***
     *
     * 播放器实现
     *
     ***/

    override fun loadReady() {
        val media = VideoManager.newInstance().getCurrPlayer(taskNum)
        media.setSurface(Surface(mSurfaceTexture!!))
        val vh = getViewHolder()
        val totalProgress = VideoManager.newInstance().getTotalProgress()
        val currProgress = VideoManager.newInstance().getCurrProgress()

        vh.tvTotalTime.text = getTime(totalProgress)
        vh.tvCurrentTime.text = getTime(currProgress)
        vh.sbProgress.max = totalProgress
        vh.sbProgress.progress = currProgress
        vh.sbProgress.secondaryProgress = 0

        vh.viewControl.visibility = View.VISIBLE
    }

    override fun loadError() {


    }

    override fun completePlayer() {
        stop()
    }

    fun getViewHolder(): VideoViewHolder {
        return viewHolder!!
    }

    /***
     *
     * 视频播放器实现
     *
     ***/

    override fun loadUrl(path: String) {
        val delay = 0L
        val period = 1000 / 24L
        CountManager.newInstance().startCount(delay, period, this)
        VideoManager.newInstance().stop()
        VideoManager.newInstance().loadUrl(path)
        getViewHolder().ivPlay.visibility = View.VISIBLE
        getViewHolder().ivPlay.setImageResource(R.mipmap.video_play_normal)
        getViewHolder().sbProgress.isEnabled = false
    }

    override fun start() {
        if (!isPrepared()) {
            Toast.makeText(context, "还未加载好", Toast.LENGTH_SHORT).show()
            return
        }
        getViewHolder().sbProgress.isEnabled = true
        isPlaying = true
        VideoManager.newInstance().start()
        getViewHolder().ivPlay.visibility = View.INVISIBLE
        getViewHolder().sbProgress.secondaryProgress = getCurrBufferProgress()

    }

    override fun pause() {
        isPlaying = false
        VideoManager.newInstance().pause()
        getViewHolder().ivPlay.visibility = View.VISIBLE
        getViewHolder().ivPlay.setImageResource(R.mipmap.video_pause_normal)
    }

    override fun stop() {
        val vh = getViewHolder()
        isPlaying = false
        VideoManager.newInstance().stop()
        CountManager.newInstance().stopCount()
        vh.ivPlay.visibility = View.VISIBLE
        vh.ivPlay.setImageResource(R.mipmap.video_play_normal)
        vh.sbProgress.progress = 0
    }

    override fun enterFull() {
        EventBus.getDefault().post(ScreenConfig(true))
        isFull = true
        handlerScreen()
    }

    override fun exitFull() {
        EventBus.getDefault().post(ScreenConfig(false))
        isFull = false
        handlerScreen()
    }

    private fun handlerScreen() {
        try {
            val childView = if (isFull) {
                formView!!.getChildAt(0) as VideoPlayer
            } else {
                toView!!.getChildAt(0) as VideoPlayer
            }

            val lp = childView.layoutParams
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT
            childView.layoutParams = lp

            formView!!.removeAllViews()
            toView!!.removeAllViews()

            if (isFull) {
                toView!!.addView(childView)
            } else {
                formView!!.addView(childView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun showLoading() {
        val vh = getViewHolder()
        vh.viewLoading.visibility = View.VISIBLE
        vh.pbLoading.showNow()
        vh.sbProgress.secondaryProgress = getCurrBufferProgress()
    }

    override fun hideLoading() {
        val vh = getViewHolder()
        vh.viewLoading.visibility = View.INVISIBLE
        vh.pbLoading.hideNow()
    }

    private fun isPrepared(): Boolean {
        return VideoManager.newInstance().isPrepared()
    }

    private fun getCurrBufferProgress(): Int {
        val total = VideoManager.newInstance().getTotalProgress()
        return (total * bufferProgress / 100)
    }

    fun isFull(): Boolean {
        return isFull
    }


    override fun mediaInfo(what: Int, extra: Int) {
        Log.e("what and extra", "" + what + "," + extra)
        if(isPlaying) {
            when (what) {
                MediaConfig.MEDIA_INFO_VIDEO_RENDERING_START -> hideLoading()
                MediaConfig.MEDIA_INFO_BUFFERING_START -> showLoading()
                MediaConfig.MEDIA_INFO_BUFFERING_END -> hideLoading()
            }
        }

    }

    override fun buffer(percent: Int) {
        Log.e("当前缓冲", "" + percent)
        bufferProgress = percent
        getViewHolder().sbProgress.secondaryProgress = getCurrBufferProgress()
    }

    override fun changeSize(width: Int, height: Int) {
        mTextureView!!.adaptVideoSize(width, height)
    }


    /***
     *
     * 计时更新进度
     *
     ***/

    override fun countTime() {
        if (isPlaying) {
            val vh = getViewHolder()
            val currProgress = VideoManager.newInstance().getCurrProgress()
            val totalProgress = VideoManager.newInstance().getTotalProgress()
            vh.tvCurrentTime.text = getTime(currProgress)
            vh.sbProgress.secondaryProgress = getCurrBufferProgress()
            vh.sbProgress.progress = currProgress
        }
    }

    /***
     *
     * 视频播放器生命周期
     *
     ***/

    override fun onResume() {
        if (hasPlaying && !isPlaying) {
            start()
        }
    }

    override fun onPause() {
        hasPlaying = isPlaying
        if (isPlaying) {
            pause()
        }
    }

    override fun onStop() {
        if (isPlaying) {
            stop()
        }
        CountManager.newInstance().stopCount()
    }

    override fun onDestroy() {
        CountManager.newInstance().onDestroy()
        VideoManager.newInstance().onDestroy()
    }

}