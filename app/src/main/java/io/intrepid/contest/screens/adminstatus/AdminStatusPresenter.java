package io.intrepid.contest.screens.adminstatus;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class AdminStatusPresenter extends BasePresenter<AdminStatusContract.View> implements AdminStatusContract.Presenter, ConfirmStartContestDialog.DialogInteractionListener {
    private int numEntriesMissing = 0;
    private int numJudgeRatingsMissing;
    private boolean contestStarted = false;
    private boolean awaitingSubmissions = true;

    AdminStatusPresenter(@NonNull AdminStatusContract.View view,
                         @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        fetchSubmittedEntries();
        fetchContestStatus();
        view.showAwaitSubmissionsIndicator(awaitingSubmissions);
    }

    @Override
    public void onBackPressed() {
        view.exitStatusScreen();
    }

    private void fetchSubmittedEntries() {
        String contestId = String.valueOf(persistentSettings.getCurrentContestId());
        Disposable disposable = restApi.getContestDetails(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    Timber.d("Received response " + response.contest.getTitle());
                    List<Entry> entries = response.contest.getEntries();
                    view.showSubmittedEntries(entries);
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());
                    view.showMessage(R.string.error_api);
                });
        disposables.add(disposable);
    }

    private void fetchContestStatus() {
        String contestId = String.valueOf(persistentSettings.getCurrentContestId());

        Disposable disposable = restApi.getContestStatus(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    Timber.d("Received response " + response.contestStatus);
                    numEntriesMissing = response.contestStatus.getNumSubmissionsMissing();
                    numJudgeRatingsMissing = response.contestStatus.getNumScoresMissing();
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());
                    view.showMessage(R.string.error_api);
                });
        disposables.add(disposable);
    }

    @Override
    public void onBottomNavigationButtonClicked() {
        if (contestStarted) {
            onEndContestClicked();
            return;
        }
        if (numEntriesMissing > 0) {
            view.showConfirmStartContestDialog();
        } else {
            advanceToJudgingIndicator();
        }
    }

    private void onEndContestClicked() {
        if (numJudgeRatingsMissing > 0) {
            view.showConfirmEndContestDialog();
        } else {
            endContest();
        }
    }

    private void endContest() {
        Disposable endContestDisposable = restApi.endContest(persistentSettings.getCurrentContestId().toString())
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                               Timber.d(" End contest response " + response.contest.toString());
                               AdminStatusPresenter.this.advanceToEndOfContestIndicator();
                           },
                           throwable -> {
                               Timber.d(throwable.getMessage());
                               view.showMessage(R.string.error_api);
                           });
        disposables.add(endContestDisposable);
    }

    private void advanceToJudgingIndicator() {
        contestStarted = true;
        view.advanceToJudgingIndicator();
    }

    private void advanceToEndOfContestIndicator() {
        contestStarted = false;
        view.showJudgingStatusIndicator(false);
        view.showEndOfContestIndicator(true);
    }

    @Override
    public void onPositiveButtonClicked(ConfirmStartContestDialog.AdminActionType actionType) {
        if (actionType == ConfirmStartContestDialog.AdminActionType.START_CONTEST) {
            advanceToJudgingIndicator();
        } else {
            endContest();
        }
    }

    @Override
    public void onNegativeButtonClicked(ConfirmStartContestDialog.AdminActionType actionType) {
        // Do nothing
    }
}
