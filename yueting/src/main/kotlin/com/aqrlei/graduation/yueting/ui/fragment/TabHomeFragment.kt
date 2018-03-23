package com.aqrlei.graduation.yueting.ui.fragment

import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.PopupWindow
import com.aqrairsigns.aqrleilib.basemvp.MvpContract
import com.aqrairsigns.aqrleilib.ui.view.AlphaListView
import com.aqrairsigns.aqrleilib.util.AppToast
import com.aqrairsigns.aqrleilib.util.DensityUtil
import com.aqrlei.graduation.yueting.R
import com.aqrlei.graduation.yueting.aidl.MusicInfo
import com.aqrlei.graduation.yueting.constant.YueTingConstant
import com.aqrlei.graduation.yueting.model.local.BookInfo
import com.aqrlei.graduation.yueting.model.local.infotool.ShareBookInfo
import com.aqrlei.graduation.yueting.model.local.infotool.ShareMusicInfo
import com.aqrlei.graduation.yueting.presenter.fragmentpresenter.TabHomePresenter
import com.aqrlei.graduation.yueting.ui.FileActivity
import com.aqrlei.graduation.yueting.ui.PdfReadActivity
import com.aqrlei.graduation.yueting.ui.TxtReadActivity
import com.aqrlei.graduation.yueting.ui.YueTingActivity
import com.aqrlei.graduation.yueting.ui.adapter.YueTingListAdapter
import kotlinx.android.synthetic.main.main_include_home_top.*
import kotlinx.android.synthetic.main.main_include_lv_content.view.*
import kotlinx.android.synthetic.main.welcome_include_yueting_top.*
import java.io.File

/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/23
 */
/*
* @param mPresenter 访问Presenter
* @param mView 访问对应的布局
* @param mContainerActivity 访问对应的Activity
* */
class TabHomeFragment : MvpContract.MvpFragment<TabHomePresenter, YueTingActivity>(),
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        View.OnClickListener {

    companion object {
        fun newInstance(): TabHomeFragment {
            val args = Bundle()
            val fragment = TabHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        removePosition = position
        when (view?.id) {
            R.id.ll_read_item -> {
                showDialog(true)
            }
            R.id.ll_music_item -> {
                showDialog(false)
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_right_listen -> {
                tv_right_listen.visibility = View.INVISIBLE
                mListView.adapter = mMusicAdapter
                tv_left_read.visibility = View.VISIBLE
                tv_title_name.text = "欣听"
            }
            R.id.tv_left_read -> {
                tv_left_read.visibility = View.INVISIBLE
                mListView.adapter = mBookAdapter
                tv_right_listen.visibility = View.VISIBLE
                tv_title_name.text = "悦读"
            }
            R.id.tv_setting -> {
                AppToast.toastShow(mContainerActivity, " TODO Setting", 1000)
                showPopWindow()
            }
            R.id.tv_file_local -> {
                jumpToFileActivity()
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, convertView: View, position: Int, id: Long) {
        when (convertView.id) {
            R.id.ll_music_item -> {
                val file = File(mMusicInfoShared.getInfo(position).albumUrl)
                if (file.exists()) {
                    isServiceStart = mMusicInfoShared.isStartService()
                    if (!isServiceStart) {
                        startMusicService(position)
                        isServiceStart = true
                    } else {
                        sendPlayBroadcast(position)
                    }
                } else {
                    AppToast.toastShow(mContainerActivity, "文件不存在", 1000)
                    removeInfo(false)
                }
            }
            R.id.ll_read_item -> {
                val file = File(mBookInfoShared.getInfo(position).path)
                if (file.exists()) {
                    if (mBookInfoShared.getInfo(position).type == "txt") {
                        TxtReadActivity.jumpToTxtReadActivity(mContainerActivity,
                                mBookInfoShared.getInfo(position))
                    }
                    if (mBookInfoShared.getInfo(position).type == "pdf") {
                        PdfReadActivity.jumpToPdfReadActivity(mContainerActivity,
                                mBookInfoShared.getInfo(position))
                    }
                } else {
                    AppToast.toastShow(mContainerActivity, "文件不存在", 1000)
                    removeInfo(true)
                }
            }
        }
    }

    private var removePosition: Int = 0
    private var isServiceStart = false
    private var mMusicInfoShared = ShareMusicInfo.MusicInfoTool
    private var mBookInfoShared = ShareBookInfo.BookInfoTool
    private lateinit var mBookAdapter: YueTingListAdapter
    private lateinit var mMusicAdapter: YueTingListAdapter
    private val mPopupWindow: PopupWindow  by lazy { createPopWindow() }
    private val mListView: AlphaListView
        get() = mView.lv_fragment_home as AlphaListView
    private val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            sendMusicInfoS(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
    override val mPresenter: TabHomePresenter
        get() = TabHomePresenter(this)
    override val layoutRes: Int
        get() = R.layout.main_fragment_home //welcome_activity_yueting


    override fun initComponents(view: View?, savedInstanceState: Bundle?) {
        super.initComponents(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        getMusicInfoFromDB()
        getBookInfoFromDB()
    }

    private fun initView() {
        mBookAdapter = YueTingListAdapter(mBookInfoShared.getInfoS(), mContainerActivity, R.layout.read_module_list_item, 0)
        mMusicAdapter = YueTingListAdapter(mMusicInfoShared.getInfoS(), mContainerActivity, R.layout.music_list_item, 1)
        mListView.adapter = mBookAdapter
        mListView.onItemClickListener = this
        mListView.onItemLongClickListener = this
        tv_left_read.visibility = View.INVISIBLE
        tv_title_name.text = "悦读"
        tv_left_read.setOnClickListener(this)
        tv_right_listen.setOnClickListener(this)
        tv_setting.setOnClickListener(this)
        tv_file_local.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        changeBookAdapter()
    }

    private fun getMusicInfoFromDB() {
        mPresenter.getMusicInfoFromDB()
    }

    private fun getBookInfoFromDB() {
        mPresenter.getBookInfoFromDB()
    }

    private fun removeInfo(flag: Boolean) {
        if (flag) {
            val path = mBookInfoShared.getInfo(removePosition).path
            mBookInfoShared.removeInfo(removePosition)
            mBookAdapter.notifyDataSetInvalidated()
            mPresenter.deleteBookItemFromDB(path)
        } else {
            val path = mMusicInfoShared.getInfo(removePosition).albumUrl
            mMusicInfoShared.removeInfo(removePosition)
            mMusicAdapter.notifyDataSetInvalidated()
            mPresenter.deleteMusicItemFromDB(path)
        }
    }

    private fun showDialog(isBook: Boolean) {
        val dialog = Dialog(mContainerActivity, R.style.BottomDialog)
        dialog.setContentView(R.layout.manage_dialog_bottom)
        dialog.window.decorView.findViewById(R.id.tv_remove_items).setOnClickListener({
            removeInfo(isBook)
            dialog.dismiss()
        })
        dialog.window.setGravity(Gravity.BOTTOM)
        dialog.window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,
                DensityUtil.dipToPx(mContainerActivity, 50f))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun startMusicService(position: Int) {
        mPresenter.startMusicService(mContainerActivity, position, Messenger(mMusicInfoShared.getHandler(mContainerActivity)), serviceConn)
    }

    private fun sendPlayBroadcast(position: Int) {
        val ACTION_PLAY = YueTingConstant.ACTION_BROADCAST[YueTingConstant.ACTION_PLAY]
        val playIntent = Intent(ACTION_PLAY)
        playIntent.putExtra("position", position)
        mContainerActivity.sendOrderedBroadcast(playIntent, null)
    }

    private fun sendMusicInfoS(binder: IBinder?) {
        if (binder != null) {
            mPresenter.sendMusicInfo(binder)
        }
    }

    fun unbindMusicService() {
        mContainerActivity.unbindService(serviceConn)
    }

    fun setMusicInfo(data: ArrayList<MusicInfo>) {
        mMusicInfoShared.setInfoS(data)
        mMusicAdapter.notifyDataSetChanged()
    }

    fun setBookInfo(data: ArrayList<BookInfo>) {
        mBookInfoShared.setInfoS(data)
        mBookAdapter.notifyDataSetChanged()

    }

    fun changeMusicAdapter() {
        mMusicAdapter.notifyDataSetInvalidated()
    }

    fun changeBookAdapter() {
        mBookAdapter.notifyDataSetInvalidated()
    }

    private fun jumpToFileActivity() {
        startActivityForResult(Intent(mContainerActivity, FileActivity::class.java),
                YueTingConstant.YUETINGRQCODE)
    }

    private fun createPopWindow(): PopupWindow {

        return PopupWindow(
                LayoutInflater.from(mContainerActivity).inflate(R.layout.main_test_popwindow, null),
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.widthPixels
        )
    }


    private fun showPopWindow() {
        if (mPopupWindow.isShowing) {
            mPopupWindow.dismiss()
        } else {
            mPopupWindow.showAsDropDown(tv_setting)
        }

    }


}