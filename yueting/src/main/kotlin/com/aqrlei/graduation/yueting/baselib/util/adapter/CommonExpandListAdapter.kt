package com.aqrlei.graduation.yueting.baselib.util.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/25
 */
/*
* @param mContext 上下文
* @param mData 传入的数据
* @param childResId 子布局Id
* @param groupResId 组布局Id
* */
abstract class CommonExpandListAdapter<T>() :
        BaseExpandableListAdapter(), View.OnClickListener {
    protected lateinit var mContext: Context
    protected lateinit var mData: List<T>
    protected var childResId: Int = 0
    protected var groupResId: Int = 0
    protected var mListener: OnInternalClick? = null

    internal constructor(mContext: Context,
                         mData: List<T>,
                         childResId: Int,
                         groupResId: Int) : this() {
        this.mContext = mContext
        this.mData = mData
        this.childResId = childResId
        this.groupResId = groupResId
    }

    internal constructor(mContext: Context,
                         mData: List<T>,
                         childResId: Int,
                         groupResId: Int,
                         listener: OnInternalClick) : this() {
        this.mContext = mContext
        this.mData = mData
        this.childResId = childResId
        this.groupResId = groupResId
        this.mListener = listener
    }

    companion object {
        private val TYPE_GROUP: Boolean = true
        private val TYPE_CHILD: Boolean = false
    }

    /*设置是否可以选中*/
    override fun isChildSelectable(groupPosition: Int, childPosition: Int) = true

    /*返回未为false时，在调用notifyDataSetChanged时只会调用那些getItem变化的getView方法*/
    override fun hasStableIds() = false

    override fun getGroupCount() = mData.size
    override fun getGroupId(groupPosition: Int) = groupPosition.toLong()
    override fun getChildId(groupPosition: Int, childPosition: Int) = childPosition.toLong()
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        val childViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, childResId,
                childPosition, convertView, parent)
        bindData(childViewHolder, mData, groupPosition, TYPE_CHILD)
        if (mListener != null) {
            setInternalClick(childViewHolder, TYPE_CHILD)
        }
        return childViewHolder.convertView
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?,
                              parent: ViewGroup): View {
        val groupViewHolder = CommonListViewHolder.getCommonViewHolder(mContext, groupResId,
                groupPosition, convertView, parent)
        bindData(groupViewHolder, mData, groupPosition, TYPE_GROUP)
        /*为true时设置groupView不可点击*/
        groupViewHolder.convertView.isClickable = true
        if (mListener != null) {
            setInternalClick(groupViewHolder, TYPE_GROUP)
        }
        return groupViewHolder.convertView
    }

    protected abstract fun bindData(holder: CommonListViewHolder, data: List<T>, groupPosition: Int,
                                    isGroup: Boolean)

    protected abstract fun setInternalClick(holder: CommonListViewHolder, type: Boolean)

    override fun onClick(v: View) {
        mListener?.onInternalClick(v)
    }

    interface OnInternalClick {
        fun onInternalClick(v: View)
    }
}