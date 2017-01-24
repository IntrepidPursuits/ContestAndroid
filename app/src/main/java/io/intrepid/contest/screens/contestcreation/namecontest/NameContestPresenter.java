package io.intrepid.contest.screens.contestcreation.namecontest;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

class NameContestPresenter extends BasePresenter<NameContestContract.View> implements NameContestContract.Presenter {

    NameContestPresenter(@NonNull NameContestContract.View view,
                         @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onContestNameUpdate(String contestName) {
        if(contestName.isEmpty()){
           showError();
        }else{
          view.saveEnteredName(contestName);
        }
    }

    private void showError() {
        view.showError();
    }
}
