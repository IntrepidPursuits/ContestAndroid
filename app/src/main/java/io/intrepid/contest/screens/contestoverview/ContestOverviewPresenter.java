package io.intrepid.contest.screens.contestoverview;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class ContestOverviewPresenter extends BasePresenter<ContestOverviewContract.View>
        implements ContestOverviewContract.Presenter {

    private static final int TEMPORARY_NUM_SUBMISSIONS_WAITING = 3;

    ContestOverviewPresenter(@NonNull ContestOverviewContract.View view,
                             @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.showRatingGuide();

        requestContestDetails();

        // TODO: this number will come from judge scoring
        view.showNumSubmissionsWaiting(TEMPORARY_NUM_SUBMISSIONS_WAITING);
    }

    private void requestContestDetails() {
        String contestId = persistentSettings.getCurrentContestId().toString();
        Disposable apiCallDisposable = restApi.getContestDetails(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    Contest contest = response.contest;
                    updateViewWithContest(contest);
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());
                    view.showMessage(R.string.error_api);
                });
        disposables.add(apiCallDisposable);
    }

    private void updateViewWithContest(Contest contest){
        view.showContestName(contest.getTitle());
        view.showContestDescription(contest.getDescription());
        view.showCategories(contest.getCategories());
    }
}
