package com.orange.domain.interactor

import com.orange.domain.executor.PostExecutionThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

// for data between data layer and domain layer,
// we need to create an interface that holds the abstraction for the use cases

abstract class ObservableUseCase<T, in Params> constructor(
        private val postExecutionThread: PostExecutionThread){

    private val disposables = CompositeDisposable()

    protected abstract fun buildUseCaseObservable(params: Params? = null) : Observable<T>

    // so that observing and subscribing will be written once
    open fun execute(observer: DisposableObserver<T>, params: Params? = null){
        val observable = this.buildUseCaseObservable(params)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.scheduler)

        addDisposable(observable.subscribeWith(observer))
    }

    fun dispose(){
        disposables.dispose()
    }

    private fun addDisposable(disposable: Disposable){
        disposables.add(disposable)
    }
}