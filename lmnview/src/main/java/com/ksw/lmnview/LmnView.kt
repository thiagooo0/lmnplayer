package com.ksw.lmnview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout

/**
 * @author KwokSiuWang
 * @date 2018/6/11
 */
class LmnView(context: Context, attributeSet: AttributeSet?, i: Int) : FrameLayout(context, attributeSet, i) {


    private lateinit var lmnPlayer: LmnPlayer

    /**
     * 是否初始化。
     */
    private var isSurfaceCreated = false

    private var onLmnReadyCallBack: OnLmnReadyCallBack? = null

    companion object {
        private const val TAG = "LmnView"
    }

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    init {

        //新建surfaceView
        val surfaceView = SurfaceView(context)
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                if (holder != null) {
                    lmnPlayer = LmnPlayer(holder)
                    onLmnReadyCallBack?.onLmnReady(lmnPlayer)
                } else {
                    onLmnReadyCallBack?.onLmnFail("init Surface fail")
                    Log.d(TAG, "init Surface fail")
                }
                isSurfaceCreated = true
//                mediaPlayer.setDisplay(surfaceView.holder)
            }

        })
        //插入到次view中
        surfaceView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.addView(surfaceView)
    }

    fun getLmnViewAsync(onLmnReadyCallBack: OnLmnReadyCallBack) {
        this.onLmnReadyCallBack = onLmnReadyCallBack
        if (isSurfaceCreated) {
            onLmnReadyCallBack.onLmnReady(lmnPlayer)
        }
    }
}