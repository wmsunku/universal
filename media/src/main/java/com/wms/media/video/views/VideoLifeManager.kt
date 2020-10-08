package com.wms.media.video.views

import com.wms.media.sdk.CountManager

 fun onResume() {
     VideoManager.newInstance().start()
 }

 fun onPause() {
     VideoManager.newInstance().pause()
}

 fun onStop() {
     VideoManager.newInstance().stop()
     CountManager.newInstance().stopCount()
}

 fun onDestroy() {
    CountManager.newInstance().onDestroy()
    VideoManager.newInstance().onDestroy()
}