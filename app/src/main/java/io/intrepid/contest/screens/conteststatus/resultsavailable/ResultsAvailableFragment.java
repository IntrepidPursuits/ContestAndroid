package io.intrepid.contest.screens.conteststatus.resultsavailable;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.rest.ContestResponse;
import io.intrepid.contest.screens.conteststatus.ContestStatusActivityContract;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class ResultsAvailableFragment extends BaseFragment<ResultsAvailableContract.Presenter>
        implements ResultsAvailableContract.View {

    ContestStatusActivityContract contestStatusActivity;

    @BindView(R.id.results_available_contest_title_text_view)
    TextView contestTitleTextView;

    @NonNull
    @Override
    public ResultsAvailableContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new ResultsAvailablePresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_results_available;
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        contestStatusActivity = (ContestStatusActivityContract) getActivity();
    }

    @OnClick(R.id.results_available_button)
    public void onViewResultsButtonClicked() {
        presenter.onViewResultsButtonClicked();
    }

    @Override
    public void showContestName(String contestName) {
        contestTitleTextView.setText(contestName);
    }

    @Override
    public void showResultsPage() {
        Timber.d("Show results page");
    }

    @Override
    public void requestContestDetails(Consumer<ContestResponse> responseCallback,
                                      Consumer<Throwable> throwableCallback) {
        contestStatusActivity.requestContestDetails(responseCallback, throwableCallback);
    }
}
