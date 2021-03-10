package uk.me.mungorae.eltinterview.rxjava

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulerProvider {

    fun io(): Scheduler

    fun main(): Scheduler

    class Default: SchedulerProvider {
        override fun io(): Scheduler = Schedulers.io()

        override fun main(): Scheduler = AndroidSchedulers.mainThread()
    }
}