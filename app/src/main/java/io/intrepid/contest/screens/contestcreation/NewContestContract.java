package io.intrepid.contest.screens.contestcreation;

import android.content.Intent;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;

public class NewContestContract {

    public interface View extends BaseContract.View {
        void navigateBackwards();
        void navigateForward();
    }

    public interface Presenter extends BaseContract.Presenter<View> {

        void onNextButtonClicked();

        void onBackButtonClicked();
    }
}
