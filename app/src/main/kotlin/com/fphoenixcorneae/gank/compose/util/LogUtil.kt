package com.fphoenixcorneae.gank.compose.util

import android.text.TextUtils
import android.util.Log

/**
 * @desc：LogUtil
 * @date：2021/09/24 11:02
 */
object LogUtil {
    private const val DEFAULT_TAG = "JetpackMvvm"
    var logEnabled = true

    fun debugInfo(tag: String? = DEFAULT_TAG, msg: String?) {
        if (!logEnabled || TextUtils.isEmpty(msg)) {
            return
        }
        Log.d(tag, msg!!)
    }

    fun warnInfo(tag: String? = DEFAULT_TAG, msg: String?) {
        if (!logEnabled || TextUtils.isEmpty(msg)) {
            return
        }
        Log.w(tag, msg!!)
    }

    /**
     * 这里使用自己分节的方式来输出足够长度的 message
     *
     * @param tag 标签
     * @param msg 日志内容
     */
    fun debugLongInfo(tag: String? = DEFAULT_TAG, msg: String) {
        var msg = msg
        if (!logEnabled || TextUtils.isEmpty(msg)) {
            return
        }
        msg = msg.trim { it <= ' ' }
        var index = 0
        val maxLength = 3500
        var sub: String
        while (index < msg.length) {
            sub = if (msg.length <= index + maxLength) {
                msg.substring(index)
            } else {
                msg.substring(index, index + maxLength)
            }
            index += maxLength
            Log.d(tag, sub.trim { it <= ' ' })
        }
    }
}