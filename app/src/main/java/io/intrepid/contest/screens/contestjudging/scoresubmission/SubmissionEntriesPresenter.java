package io.intrepid.contest.screens.contestjudging.scoresubmission;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static io.intrepid.contest.screens.contestjudging.scoresubmission.SubmissionEntriesContract.View;

class SubmissionEntriesPresenter extends BasePresenter<SubmissionEntriesContract.View>
        implements SubmissionEntriesContract.Presenter {

    SubmissionEntriesPresenter(@NonNull View view,
                               @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        fetchEntriesForContest();
    }

    private void fetchEntriesForContest() {
        String contestId = persistentSettings.getCurrentContestId().toString();
        Disposable apiCallDisposable = restApi.getContestDetails(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    Timber.d("Received response " + response.contest.getTitle());
                    Contest contest = response.contest;
                    updateViewWithContest(contest);
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());
                    view.showMessage(R.string.error_api);
                });
        disposables.add(apiCallDisposable);
    }

    private void updateViewWithContest(Contest contest) {
        view.showSubmissionList(contest.getEntries());
    }
}
