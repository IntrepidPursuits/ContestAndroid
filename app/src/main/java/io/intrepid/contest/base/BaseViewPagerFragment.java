package io.intrepid.contest.base;

public abstract class BaseViewPagerFragment<T extends BaseContract.Presenter> extends BaseFragment<T> implements TextValidatableView {
    @Override
    public boolean areAllFieldValid() {
        return true;
    }

    @Override
    public int errorMessage() {
        return 0;
    }
}
