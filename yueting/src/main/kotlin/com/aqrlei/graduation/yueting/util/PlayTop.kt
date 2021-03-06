package com.aqrlei.graduation.yueting.util

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import com.aqrlei.graduation.yueting.constant.ActionConstant
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.enumtype.SendType
import com.aqrlei.graduation.yueting.model.infotool.ShareMusicInfo

/**
 * created by AqrLei on 2018/4/6
 */
fun sendPlayBroadcast(position: Int, context: Context) {
    val playIntent = Intent(ActionConstant.ACTION_PLAY)
    playIntent.putExtra(YueTingConstant.SERVICE_MUSIC_ITEM_POSITION, position)
    context.sendOrderedBroadcast(playIntent, null)
}

fun sendMusicBroadcast(type: SendType, context: Context, code: Int = YueTingConstant.PLAY_LIST) {
    ShareMusicInfo.MusicInfoTool.sendBroadcast(context, type, code)
}

fun sendMusicRefresh(position: Int, typeName: String, context: Context) {
    val refreshIntent = Intent(ActionConstant.ACTION_REFRESH).apply {
        putExtra(ActionConstant.ACTION_REFRESH_KEY, typeName)
        putExtra(YueTingConstant.SERVICE_MUSIC_ITEM_POSITION, position)
    }
    context.sendOrderedBroadcast(refreshIntent, null)
}

fun sendKillApp(context: Context) {
    context.sendOrderedBroadcast(Intent(ActionConstant.ACTION_FINISH), null)
}


fun initPlayView(playView: ViewGroup, position: Int, duration: Int = 0) {
    ShareMusicInfo.MusicInfoTool.shareViewInit(playView, position, duration)
}