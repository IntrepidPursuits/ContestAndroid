package io.intrepid.contest.screens.conteststatus.waitingsubmissions;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.ContestResponse;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

class WaitingSubmissionsPresenter extends BasePresenter<WaitingSubmissionsContract.View>
        implements WaitingSubmissionsContract.Presenter {

    WaitingSubmissionsPresenter(@NonNull WaitingSubmissionsContract.View view,
                                @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        if (persistentSettings.getCurrentParticipationType() == ParticipationType.JUDGE) {
            view.showJudgeUI();
            view.requestContestDetails(onContestDetailsRetrieved(), onContestDetailsError());
        }
    }

    @NonNull
    private Consumer<ContestResponse> onContestDetailsRetrieved() {
        return response -> view.showContestName(response.contest.getTitle());
    }

    @NonNull
    private Consumer<Throwable> onContestDetailsError() {
        return throwable -> {
            Timber.d("API error retrieving contest details: " + throwable.getMessage());
            view.showMessage(R.string.error_api);
        };
    }

    @Override
    public void onNumSubmissionsMissingUpdated(int numSubmissionsMissing) {
        if (persistentSettings.getCurrentParticipationType() == ParticipationType.CONTESTANT) {
            view.showNumSubmissionsMissing(numSubmissionsMissing);
        }
    }
}
