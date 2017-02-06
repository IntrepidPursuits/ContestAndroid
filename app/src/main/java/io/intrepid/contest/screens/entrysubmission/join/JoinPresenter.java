package io.intrepid.contest.screens.entrysubmission.join;

import android.support.annotation.NonNull;

import java.util.UUID;

import io.intrepid.contest.BuildConfig;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.InvitationResponse;
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
                    view.showEntryNameScreen();
                });
        disposables.add(disposable);
    }

    private void showResult(InvitationResponse response) {
        if (response.contest != null) {
            persistentSettings.setCurrentContestId(response.contest.getId());
            view.showEntryNameScreen();
        } else {
            view.showInvalidCodeErrorMessage();
        }
    }
}
