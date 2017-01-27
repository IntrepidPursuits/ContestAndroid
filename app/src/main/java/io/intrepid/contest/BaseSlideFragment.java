package io.intrepid.contest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.contestcreation.reviewcontest.ReviewContestPresenter;

public abstract class BaseSlideFragment<T extends BaseContract.Presenter> extends BaseFragment{

    public abstract boolean canMoveFurther();
    public abstract int cantMoveFurtherErrorMessage();
    protected abstract int getLayoutResourceId();

    @NonNull
    public abstract T createPresenter(PresenterConfiguration configuration);
}
