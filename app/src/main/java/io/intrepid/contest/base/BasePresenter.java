package io.intrepid.contest.base;

import android.support.annotation.NonNull;

import io.intrepid.contest.rest.RestApi;
import io.intrepid.contest.settings.PersistentSettings;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter<T> {

    protected final CompositeDisposable disposables = new CompositeDisposable();

    @NonNull
    protected final Scheduler ioScheduler;
    @NonNull
    protected final Scheduler uiScheduler;
    @NonNull
    protected final PersistentSettings persistentSettings;
    @NonNull
    protected final RestApi restApi;
    protected T view;
    private boolean isViewBound = false;

    public BasePresenter(@NonNull T view, @NonNull PresenterConfiguration configuration) {
        this.view = view;
        this.ioScheduler = configuration.getIoScheduler();
        this.uiScheduler = configuration.getUiScheduler();
        this.persistentSettings = configuration.getPersistentSettings();
        this.restApi = configuration.getRestApi();
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public final void bindView(@NonNull T view) {
        this.view = view;

        if (!isViewBound) {
            onViewBound();
            isViewBound = true;
        }
    }

    protected void onViewBound() {

    }

    @Override
    public final void unbindView() {
        disposables.clear();
        this.view = null;

        if (isViewBound) {
            onViewUnbound();
            isViewBound = false;
        }
    }

    protected void onViewUnbound() {

    }

    /**
     * Note: The view will be null at this point. This method is for any additional cleanup that's not handled
     * by the CompositeSubscription
     */
    @Override
    public void onViewDestroyed() {

    }

    protected <R> ObservableTransformer<R, R> subscribeOnIoObserveOnUi() {
        return observable -> observable.subscribeOn(ioScheduler).observeOn(uiScheduler);
    }


}
