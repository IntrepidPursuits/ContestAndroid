package io.intrepid.contest.screens.join;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.RedeemInvitationRequest;
import io.intrepid.contest.rest.RedeemInvitationResponse;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class JoinPresenter extends BasePresenter<JoinContract.View> implements JoinContract.Presenter {

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
        RedeemInvitationRequest redeemInvitationRequest = new RedeemInvitationRequest(code);

        Disposable disposable = restApi.redeemInvitationCode(code, redeemInvitationRequest)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> showResult(response), throwable -> {
                    Timber.d("API error redeeming invitation code: " + throwable.getMessage());
                    view.showMessage(R.string.error_api);
                });
        disposables.add(disposable);
    }

    private void showResult(RedeemInvitationResponse response) {
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
