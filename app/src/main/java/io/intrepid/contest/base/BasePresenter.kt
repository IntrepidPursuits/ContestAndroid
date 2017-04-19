package io.intrepid.contest.base

import io.intrepid.contest.rest.RestApi
import io.intrepid.contest.settings.PersistentSettings
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : BaseContract.View>(protected var view: T?, configuration: PresenterConfiguration) : BaseContract.Presenter<T> {

    protected val disposables = CompositeDisposable()

    protected val ioScheduler: Scheduler = configuration.ioScheduler
    protected val uiScheduler: Scheduler = configuration.uiScheduler
    protected val persistentSettings: PersistentSettings = configuration.persistentSettings
    protected val restApi: RestApi = configuration.restApi
    private var isViewBound = false

    override fun onViewCreated() {

    }

    override fun bindView(view: T) {
        this.view = view

        if (!isViewBound) {
            onViewBound()
            isViewBound = true
        }
    }

    protected open fun onViewBound() {

    }

    override fun unbindView() {
        disposables.clear()
        this.view = null

        if (isViewBound) {
            onViewUnbound()
            isViewBound = false
        }
    }

    protected fun onViewUnbound() {

    }

    /**
     * Note: The view will be null at this point. This method is for any additional cleanup that's not handled
     * by the CompositeSubscription
     */
    override fun onViewDestroyed() {

    }

    protected fun <R> subscribeOnIoObserveOnUi(): ObservableTransformer<R, R> {
        return ObservableTransformer { observable -> observable.subscribeOn(ioScheduler).observeOn(uiScheduler) }
    }

}
