package com.aqrlei.graduation.yueting.basemvp

import io.reactivex.disposables.Disposable


/**
 *@Author: AqrLei
 *@Date: 2017/8/22
 */
abstract class BasePresenter {
    private var disposablesList = ArrayList<Disposable>()
    fun addDisposables(disposable: Disposable) {
        disposablesList.add(disposable)
    }

    fun finish() {
        disposablesList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}