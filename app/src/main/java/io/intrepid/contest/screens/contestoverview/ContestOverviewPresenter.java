package io.intrepid.contest.screens.contestoverview;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class ContestOverviewPresenter extends BasePresenter<ContestOverviewContract.View>
        implements ContestOverviewContract.Presenter {

    private String contestId;

    public ContestOverviewPresenter(@NonNull ContestOverviewContract.View view,
                                    @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        contestId = persistentSettings.getCurrentContestId().toString();
        requestContestDetails();
        requestContestStatus();
    }

    private void requestContestDetails() {
        Disposable apiCallDisposable = restApi.getContestDetails(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    view.showContestName(response.contest.getTitle());
                }, throwable -> {
                    Timber.d("API error retrieving contest details: " + throwable.getMessage());

                    view.showMessage(R.string.error_api);

                    // TODO: remove this fallback once API works
                    view.showContestName("Chili cookoff");
                });
        disposables.add(apiCallDisposable);
    }

    private void requestContestStatus() {
        Disposable apiCallDisposable = restApi.getContestStatus(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    view.showNumSubmissionsMissing(response.numSubmissionsMissing);
                }, throwable -> {
                    Timber.d("API error retrieving contest status: " + throwable.getMessage());

                    // TODO: once API endpoing works, stop showing message and showing hardcoded number of submissions
                    view.showMessage(R.string.error_api);
                    view.showNumSubmissionsMissing(5);
                });
        disposables.add(apiCallDisposable);
    }
}
