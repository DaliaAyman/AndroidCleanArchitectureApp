package com.orange.domain.interactor

import com.orange.domain.executor.PostExecutionThread
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

// for data between data layer and domain layer,
// we need to create an interface that holds the abstraction for the use cases

abstract class CompletableUseCase<in Params> constructor(
        private val postExecutionThread: PostExecutionThread){

    private val disposables = CompositeDisposable()

    protected abstract fun buildUseCaseCompletable(params: Params? = null) : Completable

    // so that observing and subscribing will be written once
    open fun execute(observer: DisposableCompletableObserver, params: Params? = null){
        val completable = this.buildUseCaseCompletable(params)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.scheduler)

        addDisposable(completable.subscribeWith(observer))
    }

    fun dispose(){
        disposables.dispose()
    }

    private fun addDisposable(disposable: Disposable){
        disposables.add(disposable)
    }
}