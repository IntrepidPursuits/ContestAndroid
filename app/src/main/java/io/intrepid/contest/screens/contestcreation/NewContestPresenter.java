package io.intrepid.contest.screens.contestcreation;

import android.content.Intent;
import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import timber.log.Timber;

public class NewContestPresenter extends BasePresenter<NewContestContract.View> implements NewContestContract.Presenter {

    private int pageIndex = 0;
    private Contest.Builder contestBuilder;

    public NewContestPresenter(@NonNull NewContestContract.View view,
                               @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }


//    @Override
//    public void onNextButtonClicked() {
//        if (canAdvanceToNextScreen()) {
//            view.advanceToNextScreen();
//            pageIndex++;
//        }
//    }
//
//    @Override
//    public void onBackButtonClicked() {
//        if (canReturnToPreviousSceen()) {
//            view.returnToPreviousScreen();
//            pageIndex--;
//        } else {
//            view.onCancelClicked();
//        }
//    }

    public void initializeContestBuilder(Intent intent) {
        contestBuilder = new Contest.Builder();
    }

    @Override
    public void onNextButtonClicked() {

    }

    @Override
    public void onBackButtonClicked() {

    }
}
