package io.intrepid.contest.screens.conteststatus;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.ContestStatusResponse;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class ContestStatusPresenter extends BasePresenter<ContestStatusContract.View> implements ContestStatusContract.Presenter {
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
        Timber.d("Contest status API call.");

        String contestId = persistentSettings.getCurrentContestId().toString();

        Disposable disposable = restApi.getContestStatus(contestId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> showContestStatusScreen(response), throwable -> {
                    Timber.d("API error checking contest status: " + throwable.getMessage());

                    // TODO: once API endpoing works, stop showing message and skipping to next screen
                    view.showMessage(R.string.error_api);
                    view.showWaitingSubmissionsFragment(3);
                });
        disposables.add(disposable);
    }

    private void showContestStatusScreen(ContestStatusResponse response) {
        if (response.waitingForSubmissions) {
            view.showWaitingSubmissionsFragment(response.numSubmissionsMissing);
        }
    }
}
