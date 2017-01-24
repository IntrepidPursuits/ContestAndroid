package io.intrepid.contest.splash;

import io.intrepid.contest.base.BaseContract;

public interface SplashContract {
    interface View extends BaseContract.View {
        void showMessage(String string);
    }
    interface Presenter extends BaseContract.Presenter {
        void exampleButtonClicked();
    }
}
