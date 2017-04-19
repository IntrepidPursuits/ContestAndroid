package io.intrepid.contest.screens.adminstatus;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.ParticipationType;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class AdminStatusPresenter extends BasePresenter<AdminStatusContract.View> implements AdminStatusContract.Presenter, ConfirmStartContestDialog.DialogInteractionListener {
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
        getView().showAwaitSubmissionsIndicator(awaitingSubmissions);
    }

    @Override
    public void onBackPressed() {
        getView().exitStatusScreen();
    }

    private void fetchSubmittedEntries() {
        String contestId = String.valueOf(getPersistentSettings().getCurrentContestId());
        Disposable disposable = getRestApi().getContestDetails(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    Timber.d("Received response " + response.contest.getTitle());
                    List<Entry> entries = response.contest.getEntries();
                    getView().showSubmittedEntries(entries);
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());
                    getView().showMessage(R.string.error_api);
                });
        getDisposables().add(disposable);
    }

    private void fetchContestStatus() {
        String contestId = String.valueOf(getPersistentSettings().getCurrentContestId());

        Disposable disposable = getRestApi().getContestStatus(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    Timber.d("Received response " + response.contestStatus);
                    numEntriesMissing = response.contestStatus.getNumSubmissionsMissing();
                    numJudgeRatingsMissing = response.contestStatus.getNumScoresMissing();
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());
                    getView().showMessage(R.string.error_api);
                });
        getDisposables().add(disposable);
    }

    @Override
    public void onBottomNavigationButtonClicked() {
        if (contestStarted) {
            onEndContestClicked();
            return;
        }
        if (numEntriesMissing > 0) {
            getView().showConfirmStartContestDialog();
        } else {
            closeSubmissions();
        }
    }

    private void onEndContestClicked() {
        if (numJudgeRatingsMissing > 0) {
            getView().showConfirmEndContestDialog();
        } else {
            endContest();
        }
    }

    private void endContest() {
        Disposable endContestDisposable = getRestApi().endContest(getPersistentSettings().getCurrentContestId().toString())
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                               Timber.d(" End contest response " + response.contest.toString());
                               AdminStatusPresenter.this.advanceToEndOfContestIndicator();
                           },
                           throwable -> {
                               Timber.d(throwable.getMessage());
                               getView().showMessage(R.string.error_api);
                           });
        getDisposables().add(endContestDisposable);
    }

    private void closeSubmissions() {
        Disposable closeSubmissionsDisposable = getRestApi().closeSubmissions(getPersistentSettings().getCurrentContestId().toString()) .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                               Timber.d("Contest submission closed at " + response.contest.getSubmissionsClosedAt());
                                advanceToJudgingIndicator();
                           },
                           throwable -> {
                               Timber.d(throwable.getMessage());
                               getView().showMessage(R.string.error_api);
                           });
        getDisposables().add(closeSubmissionsDisposable);
    }

    private void advanceToJudgingIndicator() {
        contestStarted = true;
        getView().advanceToJudgingIndicator();
    }

    private void advanceToEndOfContestIndicator() {
        contestStarted = false;
        getView().showJudgingStatusIndicator(false);
        getView().showEndOfContestIndicator(true);
    }

    @Override
    public void onPositiveButtonClicked(ConfirmStartContestDialog.AdminActionType actionType) {
        if (actionType == ConfirmStartContestDialog.AdminActionType.START_CONTEST) {
           closeSubmissions();
        } else {
            endContest();
        }
    }

    @Override
    public void onNegativeButtonClicked(ConfirmStartContestDialog.AdminActionType actionType) {
        // Do nothing
    }
}
