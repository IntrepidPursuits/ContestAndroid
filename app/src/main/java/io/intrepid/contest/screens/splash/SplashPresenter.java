package io.intrepid.contest.screens.splash;

import android.support.annotation.NonNull;

import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.ParticipationType;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    SplashPresenter(@NonNull SplashContract.View view,
                    @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        persistentSettings.setCurrentParticipationType(ParticipationType.CREATOR);
        authenticateUser();
    }

    private void authenticateUser() {
        if (persistentSettings.isAuthenticated()) {
            discoverOngoingContests(persistentSettings.getAuthenticationToken());
            view.showUserButtons();
        } else {
            Disposable disposable = restApi.createUser()
                    .compose(subscribeOnIoObserveOnUi())
                    .subscribe(response -> {
                        String userId = response.user.getId().toString();
                        persistentSettings.setAuthenticationToken(userId);
                        discoverOngoingContests(userId);
                        view.showUserButtons();
                    }, throwable -> {
                        Timber.d("API error creating user: " + throwable.getMessage());
                        view.showMessage(R.string.error_api);
                    });
            disposables.add(disposable);
        }
    }

    private void discoverOngoingContests(String userId) {
        Disposable disposable = restApi.getActiveContests(userId)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    List<Contest> contests = response.getContests();
                    view.showOngoingContests(contests);
                }, throwable -> view.showMessage(R.string.error_api));
        disposables.add(disposable);
    }

    @Override
    public void onCreateContestClicked() {
        view.showCreateContestScreen();
    }

    @Override
    public void onJoinContestClicked() {
        view.showJoinContestScreen();
    }

}
