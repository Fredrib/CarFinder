package com.sixt.task.util

import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

sealed class SchedulerProvider {
    object Default: SchedulerProvider()
    object Testing: SchedulerProvider()

    fun <T> applySchedulers(): SingleTransformer<T, T>? {
        val subscribeScheduler = if (this is Testing) Schedulers.trampoline() else Schedulers.io()
        val observeScheduler = if (this is Testing) Schedulers.trampoline() else AndroidSchedulers.mainThread()

        return SingleTransformer { single: Single<T> ->
            single
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
        }
    }

}