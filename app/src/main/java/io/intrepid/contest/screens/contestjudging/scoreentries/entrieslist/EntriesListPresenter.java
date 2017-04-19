package io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.rest.AdjudicateRequest;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class EntriesListPresenter extends BasePresenter<EntriesListContract.View> implements EntriesListContract.Presenter {

    EntriesListPresenter(@NonNull EntriesListContract.View view,
                         @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().setNextVisible(false);
        getView().showSubmitButton(hasCompletedScores());
        boolean reviewMode = hasCompletedScores();
        getView().showEntriesList(reviewMode);
    }

    private boolean hasCompletedScores() {
        for (Entry entry : getView().getEntries()) {
            if (!entry.isCompletelyScored()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onSubmitButtonClicked() {
        String contestId = getPersistentSettings().getCurrentContestId().toString();
        AdjudicateRequest adjudicateRequest = new AdjudicateRequest(getView().getEntryBallots());

        Disposable disposable = getRestApi().adjudicate(contestId, adjudicateRequest)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                               if (response.isSuccessful()) {
                                   getView().showContestStatusScreen();
                                   return;
                               }

                               Timber.d("API error adjudicating: " + response.message());
                               getView().showMessage(R.string.error_api);
                           },
                           throwable -> {
                               Timber.d("Retrofit error adjudicating");
                               getView().showMessage(R.string.error_api);
                           });
        getDisposables().add(disposable);
    }
}
