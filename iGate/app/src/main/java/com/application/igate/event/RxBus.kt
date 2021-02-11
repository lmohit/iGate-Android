package com.application.igate.event

import io.reactivex.rxjava3.subjects.PublishSubject

class RxBus {
    companion object {
        private val subject: PublishSubject<Any> = PublishSubject.create()

        fun publish(msg: Any) = subject.onNext(msg)

        fun getObservable() = subject
    }
}