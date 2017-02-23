package io.intrepid.contest.screens.contestjudging.scoreentries.entrieslist;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.rest.AdjudicateRequest;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class EntriesListPresenter extends BasePresenter<EntriesListContract.View> implements EntriesListContract.Presenter {

    EntriesListPresenter(@NonNull EntriesListContract.View view,
                         @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        view.setNextVisible(false);
        view.showSubmitButton(hasCompletedScores());
        view.showEntriesList();
    }

    private boolean hasCompletedScores() {
        for (Entry entry : view.getEntries()) {
            if (entry.getRatingAverage() == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onSubmitButtonClicked() {
        String contestId = persistentSettings.getCurrentContestId().toString();
        AdjudicateRequest adjudicateRequest = new AdjudicateRequest(view.getEntryBallots());

        Disposable disposable = restApi.adjudicate(contestId, adjudicateRequest)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> {
                               if (response.isSuccessful()) {
                                   view.showContestStatusScreen();
                                   return;
                               }

                               Timber.d("API error adjudicating: " + response.message());
                               view.showMessage(R.string.error_api);
                           },
                           throwable -> {
                               Timber.d("Retrofit error adjudicating");
                               view.showMessage(R.string.error_api);
                           });
        disposables.add(disposable);
    }
}
