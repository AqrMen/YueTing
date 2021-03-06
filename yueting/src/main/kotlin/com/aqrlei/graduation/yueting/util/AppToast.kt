package com.aqrlei.graduation.yueting.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast

/**
 *@Author: AqrLei
 *@Date: 2017/8/22
 */
object AppToast {
    fun toastShow(context: Context, content: String, time: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER) {
        val toast = Toast.makeText(context, content, time)

        toast.setGravity(gravity, 0, 0)
        toast.show()
    }
}