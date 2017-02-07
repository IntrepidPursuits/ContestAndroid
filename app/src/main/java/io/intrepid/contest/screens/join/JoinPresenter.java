package io.intrepid.contest.screens.join;

import android.support.annotation.NonNull;

import java.util.UUID;

import io.intrepid.contest.BuildConfig;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Participant;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.InvitationResponse;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class JoinPresenter extends BasePresenter<JoinContract.View> implements JoinContract.Presenter {

    public static final String TEMPORARY_JUDGE_CODE = "judge";

    JoinPresenter(@NonNull JoinContract.View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onEntryCodeTextChanged(String newCode) {
        if (newCode.isEmpty()) {
            view.hideSubmitButton();
        } else {
            view.showSubmitButton();
        }
    }

    @Override
    public void onSubmitButtonClicked(String code) {
        Disposable disposable = restApi.redeemInvitationCode(code)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> showResult(response), throwable -> {
                    Timber.d("API error redeeming invitation code: " + throwable.getMessage());

                    // TODO: remove creation of fake contest once API endpoint works
                    if (BuildConfig.DEBUG) {
                        persistentSettings.setCurrentContestId(UUID.randomUUID());
                    }

                    // TODO: once API endpoing works, stop showing message and skipping to next screen
                    view.showMessage(R.string.error_api);
                    temporarySkipToNextScreen(code);
                });
        disposables.add(disposable);
    }

    private void temporarySkipToNextScreen(String code) {
        // TODO: once API endpoing works, stop showing message and skipping to next screen
        InvitationResponse response = new InvitationResponse();
        response.participant = new Participant();
        response.participant.setContestId(UUID.randomUUID());
        if (code.equals(TEMPORARY_JUDGE_CODE)) {
            response.participant.setParticipationType(ParticipationType.JUDGE);
        } else {
            response.participant.setParticipationType(ParticipationType.CONTESTANT);
        }
        showResult(response);
    }

    private void showResult(InvitationResponse response) {
        if (response.participant == null) {
            view.showInvalidCodeErrorMessage();
            return;
        }

        persistentSettings.setCurrentContestId(response.participant.getContestId());
        persistentSettings.setCurrentParticipationType(response.participant.getParticipationType());

        if (response.participant.getParticipationType() == ParticipationType.CONTESTANT) {
            view.showEntryNameScreen();
        } else {
            view.showContestStatusScreen();
        }
    }
}
