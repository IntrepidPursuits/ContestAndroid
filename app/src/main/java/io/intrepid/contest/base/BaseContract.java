package io.intrepid.contest.base;

import android.support.annotation.StringRes;

public class BaseContract {

    public interface View {
        void showMessage(String message);

        void showMessage(@StringRes int messageResource);
    }

    public interface Presenter<T extends View> {

        void bindView(T view);

        void unbindView();

        void onViewCreated();

        void onViewDestroyed();
    }
}
