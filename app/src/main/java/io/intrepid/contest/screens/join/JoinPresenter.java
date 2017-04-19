package io.intrepid.contest.screens.join;

import android.support.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.net.HttpURLConnection;
import java.util.regex.Pattern;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.RedeemInvitationRequest;
import io.intrepid.contest.rest.RedeemInvitationResponse;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class JoinPresenter extends BasePresenter<JoinContract.View> implements JoinContract.Presenter {

    JoinPresenter(@NonNull JoinContract.View view, @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();
        String clipboardData = getView().getLastCopiedText();
        if (clipboardData != null && isPotentialValidCode(clipboardData)) {
            getView().showClipboardData(clipboardData);
        }
    }

    private boolean isPotentialValidCode(String clipboardData) {
        //Verify 7-lettered input with capital and lowercase text
        String capsMatch = ".*[A-Z].*";
        String lowerCaseMatch = ".*[a-z].*";
        boolean capsSatisfied = Pattern.compile(capsMatch).matcher(clipboardData).matches();
        boolean lowerCaseSatisfied = Pattern.compile(lowerCaseMatch).matcher(clipboardData).matches();
        return clipboardData.length() == 7 && capsSatisfied && lowerCaseSatisfied &&
                !clipboardData.contains(" ");
    }

    @Override
    public void onEntryCodeTextChanged(String newCode) {
        if (newCode.isEmpty()) {
            getView().hideSubmitButton();
        } else {
            getView().showSubmitButton();
        }
    }

    @Override
    public void onSubmitButtonClicked(String code) {
        RedeemInvitationRequest redeemInvitationRequest = new RedeemInvitationRequest(code);

        Disposable disposable = getRestApi().redeemInvitationCode(code, redeemInvitationRequest)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(this::showResult, throwable -> {
                    Timber.d("API error redeeming invitation code: " + throwable.getMessage());
                    if (throwable instanceof HttpException &&
                            ((HttpException) throwable).code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        getView().showInvalidCodeErrorMessage();
                        return;
                    }
                    getView().showMessage(R.string.error_api);
                });
        getDisposables().add(disposable);
    }

    @Override
    public void onBackPressed() {
        getView().cancelJoinContest();
    }

    private void showResult(RedeemInvitationResponse response) {
        if (response.participant == null) {
            getView().showInvalidCodeErrorMessage();
            return;
        }

        getPersistentSettings().setCurrentContestId(response.participant.getContestId());
        getPersistentSettings().setCurrentParticipationType(response.participant.getParticipationType());

        if (response.participant.getParticipationType() == ParticipationType.CONTESTANT) {
            getView().showEntryNameScreen();
        } else {
            getView().showContestStatusScreen();
        }
    }
}
