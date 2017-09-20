package com.aqrlei.graduation.yueting.model.local.infotool

import com.aqrlei.graduation.yueting.model.local.MusicInfo

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @CreateTime: Date: 2017/9/20 Time: 9:06
 */
/*
* 音乐信息共享单例
* */
enum class ShareMusicInfo {

    MusicInfoTool;

    fun setInfoS(infoS: ArrayList<MusicInfo>) {
        clearAll()
        musicInfoList.addAll(infoS)
    }

    fun getInfo(position: Int) = musicInfoList[position]

    fun getInfoS() = musicInfoList

    // TODO addInfo()

    fun removeInfo(info: MusicInfo) {
        if (!musicInfoList.isEmpty()) {
            musicInfoList.remove(info)
        }
    }

    fun removeInfo(position: Int) {
        if (!musicInfoList.isEmpty()) {
            musicInfoList.removeAt(position)
        }
    }

    fun getSize() = musicInfoList.size

    private fun clearAll() {
        musicInfoList.clear()
    }


    companion object {
        private var musicInfoList = ArrayList<MusicInfo>()
    }

}