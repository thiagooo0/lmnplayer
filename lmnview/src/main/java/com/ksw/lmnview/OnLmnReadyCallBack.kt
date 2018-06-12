package com.ksw.lmnview

/**
 * @author KwokSiuWang
 * @date 2018/6/12
 */
interface OnLmnReadyCallBack {
    fun onLmnReady(lmnPlayer: LmnPlayer)
    fun onLmnFail(error: String)
}