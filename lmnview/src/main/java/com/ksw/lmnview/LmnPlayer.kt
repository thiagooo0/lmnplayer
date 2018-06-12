package com.ksw.lmnview

import android.util.Log
import android.view.SurfaceHolder
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * @author KwokSiuWang
 * @date 2018/6/12
 */
class LmnPlayer(surfaceHolder: SurfaceHolder) {
    companion object {
        const val TAG = "LmnPlayer"
    }

    /**
     * 是否成功初始化
     */
    private var isInit = true

    private val ijkMediaPlayer = IjkMediaPlayer()

    init {
        //加载so文件
        try {
            IjkMediaPlayer.loadLibrariesOnce(null)
            IjkMediaPlayer.native_profileBegin("libijkplayer.so")
            Log.d(TAG, "Init native file success")
        } catch (e: Exception) {
            isInit = false
            Log.d(TAG, "Init native file fail")
        }
        //设置log的等级
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
        //硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
        ijkMediaPlayer.setDisplay(surfaceHolder)
    }

    fun load(path: String) {
        ijkMediaPlayer.dataSource = path
        ijkMediaPlayer.prepareAsync()
    }
}