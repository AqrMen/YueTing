package com.aqrairsigns.aqrleilib.adapter

import android.content.Context
import android.widget.BaseAdapter

/**
 * @Author: AqrLei
 * @Date: 2017/8/25
 */
abstract class CommonListTypeAdapter<T>(protected var mContext: Context,
                                        protected var resId: List<Int>,
                                        protected var mData: List<T>) : BaseAdapter() {

    override fun getViewTypeCount() = resId.size
    override fun getItemId(position: Int) = position.toLong()

}