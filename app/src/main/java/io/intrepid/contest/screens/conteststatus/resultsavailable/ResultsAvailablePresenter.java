package io.intrepid.contest.screens.conteststatus.resultsavailable;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.ContestResponse;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class ResultsAvailablePresenter extends BasePresenter<ResultsAvailableContract.View>
        implements ResultsAvailableContract.Presenter {

    public ResultsAvailablePresenter(@NonNull ResultsAvailableContract.View view,
                                     @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        view.requestContestDetails(onContestDetailsRetrieved(), onContestDetailsError());
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

            // TODO: remove this fallback once API works
            view.showContestName("Chili cookoff");
        };
    }

    @Override
    public void onViewResultsButtonClicked() {
        view.showResultsPage();
        view.showMessage("Show results page");
    }
}
