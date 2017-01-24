package io.intrepid.contest.base;

import android.support.annotation.NonNull;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.rest.RestApi;

public abstract class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter<T> {

    protected T view;
    protected final RestApi restApi;

    private boolean isViewBound = false;

    public BasePresenter(@NonNull T view, @NonNull PresenterConfiguration configuration) {
        this.view = view;
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
}
