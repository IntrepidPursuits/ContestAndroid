package io.intrepid.contest.screens.conteststatus;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.intrepid.contest.BuildConfig;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.ContestStatusResponse;
import io.intrepid.contest.rest.ContestWrapper;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

class ContestStatusPresenter extends BasePresenter<ContestStatusContract.View> implements ContestStatusContract.Presenter {

    private static final int API_CALL_INITIAL_DELAY = 0;
    /*
     * API call interval is shorter for dev builds, a little longer for QA builds, and longest for release
     */
    private static final int API_CALL_INTERVAL = (BuildConfig.DEV_BUILD ? 3 : (BuildConfig.DEBUG ? 10 : 2));
    private static final TimeUnit API_CALL_INTERVAL_UNIT = (BuildConfig.DEBUG ? TimeUnit.SECONDS : TimeUnit.MINUTES);

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
        if (response.contestStatus.hasContestEnded()) {
            view.showResultsAvailableFragment();
            disposables.clear();
            return;
        }

        if (!response.contestStatus.haveSubmissionsEnded() ||
                (persistentSettings.getCurrentParticipationType() != ParticipationType.JUDGE)) {
            view.showStatusWaitingFragment();
            return;
        }

        view.showContestOverviewPage();
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
