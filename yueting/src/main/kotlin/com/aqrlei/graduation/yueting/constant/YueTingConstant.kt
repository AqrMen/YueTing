package com.aqrlei.graduation.yueting.constant

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
object YueTingConstant {

    /*fragment tag*/
    val TAB_FRAGMENT_TAGS = arrayOf(
            "tag_fragment_home",
            "tag_fragment_read",
            "tag_fragment_music"
    )
    val TAG_FRAGMENT_HOME: Int = 0
    val TAG_FRAGMENT_READ: Int = 1
    val TAG_FRAGMENT_MUSIC: Int = 2
    /*database*/
    val DB_NAME = "yueting.db"
    val MUSIC_TABLE_NAME = "musicInfo"
    val MUSIC_TABLE_C = arrayOf(
            "path",
            "type",
            "fileInfo"
    )
    val MUSIC_TABLE_C_TYPE = arrayOf(
            "varchar unique not null",
            "integer default 0",
            "text"
    )
    /*sharedpreference*/
    val SF_NAME = "yueting"
    /*BroadcastReceiverAction*/
    val ACTION_BROADCAST = arrayOf(
            "play",
            "next",
            "previous",
            "finish",
            "single",
            "list",
            "random"
    )
    val ACTION_PLAY = 0
    val ACTION_NEXT = 1
    val ACTION_PREVIOUS = 2
    val ACTION_FINISH = 3
    val ACTION_SINGLE = 4
    val ACTION_LIST = 5
    val ACTION_RANDOM = 6
    val ACTION_REQ_CODE = arrayOf(
            1, 2, 3, 4, 5, 6, 7
    )
}