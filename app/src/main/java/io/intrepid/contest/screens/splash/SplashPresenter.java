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

        getPersistentSettings().setCurrentParticipationType(ParticipationType.CREATOR);
        authenticateUser();
    }

    private void authenticateUser() {
        if (getPersistentSettings().isAuthenticated()) {
            discoverOngoingContests();
            getView().showUserButtons();
        } else {
            Disposable disposable = getRestApi().createUser()
                    .compose(subscribeOnIoObserveOnUi())
                    .subscribe(response -> {
                        String userId = response.user.getId().toString();
                        getPersistentSettings().setAuthenticationToken(userId);
                        discoverOngoingContests();
                        getView().showUserButtons();
                    }, throwable -> {
                        Timber.d("API error creating user: " + throwable.getMessage());
                        getView().showMessage(R.string.error_api);
                    });
            getDisposables().add(disposable);
        }
    }

    private void discoverOngoingContests() {
        Disposable disposable = getRestApi().getActiveContests()
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                    List<Contest> contests = response.getContests();
                    getView().showOngoingContests(contests);
                }, throwable -> getView().showMessage(R.string.error_api));
        getDisposables().add(disposable);
    }

    @Override
    public void onCreateContestClicked() {
        getView().showCreateContestScreen();
    }

    @Override
    public void onJoinContestClicked() {
        getView().showJoinContestScreen();
    }

    @Override
    public void onContestClicked(Contest contest) {
        getPersistentSettings().setCurrentContestId(contest.getId());
        ParticipationType participationType = contest.getParticipationType();
        getPersistentSettings().setCurrentParticipationType(
                participationType == null ? ParticipationType.CREATOR : participationType);
        getView().resumeContest(contest);
    }
}
