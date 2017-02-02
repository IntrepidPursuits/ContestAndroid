package io.intrepid.contest.screens.entrysubmission.join;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.InvitationResponse;
import io.reactivex.disposables.Disposable;

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
                    view.showApiErrorMessage();
                });
        disposables.add(disposable);
    }

    private void showResult(InvitationResponse response) {
        if (response.id != null) {
            view.showEntryNameScreen();
        } else {
            view.showInvalidCodeErrorMessage();
        }
    }
}
