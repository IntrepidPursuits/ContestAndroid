package io.intrepid.contest.screens.conteststatus;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.ContestStatusResponse;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class ContestStatusPresenter extends BasePresenter<ContestStatusContract.View> implements ContestStatusContract.Presenter {

    private static final int API_CALL_INITIAL_DELAY = 0;
    private static final int API_CALL_INTERVAL = 2;
    private static final TimeUnit API_CALL_INTERVAL_UNIT = TimeUnit.MINUTES;

    ContestStatusPresenter(@NonNull ContestStatusContract.View view,
                           @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        checkContestStatus();
    }

    private void checkContestStatus() {
        Timber.d("Check contest status");

        String contestId = persistentSettings.getCurrentContestId().toString();

        Disposable timerDisposable = Observable
                .interval(API_CALL_INITIAL_DELAY, API_CALL_INTERVAL, API_CALL_INTERVAL_UNIT, ioScheduler)
                .subscribe(success -> {
                    Timber.d("Contest status API call");
                    Disposable apiCallDisposable = restApi.getContestStatus(contestId)
                            .compose(subscribeOnIoObserveOnUi())
                            .subscribe(response -> showContestStatusScreen(response), throwable -> {
                                // TODO: once API endpoing works, stop showing message and skipping to next screen
                                view.showMessage(R.string.error_api);
                                view.showWaitingSubmissionsFragment(3);
                            });
                    disposables.add(apiCallDisposable);
                });
        disposables.add(timerDisposable);
    }

    private void showContestStatusScreen(ContestStatusResponse response) {
        if (response.waitingForSubmissions) {
            view.showWaitingSubmissionsFragment(response.numSubmissionsMissing);
        }
    }
}
