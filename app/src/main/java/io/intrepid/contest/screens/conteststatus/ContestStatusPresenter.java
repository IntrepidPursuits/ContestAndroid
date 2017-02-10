package io.intrepid.contest.screens.conteststatus;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.rest.ContestStatusResponse;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

class ContestStatusPresenter extends BasePresenter<ContestStatusContract.View> implements ContestStatusContract.Presenter {

    private static final int API_CALL_INITIAL_DELAY = 0;
    private static final int API_CALL_INTERVAL = 3; // TODO: change to 2 for release
    private static final TimeUnit API_CALL_INTERVAL_UNIT = TimeUnit.SECONDS; // TODO: change to MINUTES for release

    ContestStatusPresenter(@NonNull ContestStatusContract.View view,
                           @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        checkContestStatusPeriodically();
    }

    private void checkContestStatusPeriodically() {
        Timber.d("Check contest status");

        String contestId = persistentSettings.getCurrentContestId().toString();

        Disposable timerDisposable = Observable
                .interval(API_CALL_INITIAL_DELAY, API_CALL_INTERVAL, API_CALL_INTERVAL_UNIT, ioScheduler)
                .subscribe(success -> {
                    Timber.d("Contest status API call");
                    Disposable apiCallDisposable = restApi.getContestStatus(contestId)
                            .compose(subscribeOnIoObserveOnUi())
                            .subscribe(response -> showContestStatusScreen(response), throwable -> {
                                Timber.d("API error retrieving contest status: " + throwable.getMessage());
                                view.showMessage(R.string.error_api);
                            });
                    disposables.add(apiCallDisposable);
                });
        disposables.add(timerDisposable);
    }

    private void showContestStatusScreen(ContestStatusResponse response) {
        if (!response.haveSubmissionsEnded()) {
            view.showWaitingSubmissionsFragment(response.getNumSubmissionsMissing());
            return;
        }

        if (!response.hasContestEnded() &&
                (persistentSettings.getCurrentParticipationType() == ParticipationType.JUDGE)) {
            view.showContestOverviewPage();
            return;
        }

        // TODO: there is a story to show contest overview for contestants when waiting for scores
        view.showResultsAvailableFragment();
    }

    @Override
    public void requestContestDetails(Consumer<ContestWrapper> responseCallback,
                                      Consumer<Throwable> throwableCallback) {
        String contestId = persistentSettings.getCurrentContestId().toString();
        Disposable apiCallDisposable = restApi.getContestDetails(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(responseCallback, throwableCallback);
        disposables.add(apiCallDisposable);
    }
}
