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
import io.intrepid.contest.rest.ContestWrapper;
import io.intrepid.contest.screens.contestresults.ContestResultsActivity;
import io.intrepid.contest.screens.conteststatus.ContestStatusActivityContract;
import io.reactivex.functions.Consumer;

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

        setActionBarTitle(R.string.contest_status_bar_title);
        setActionBarDisplayHomeAsUpEnabled(true);

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
        startActivity(ContestResultsActivity.makeIntent(getActivity()));
    }

    @Override
    public void requestContestDetails(Consumer<ContestWrapper> responseCallback,
                                      Consumer<Throwable> throwableCallback) {
        contestStatusActivity.requestContestDetails(responseCallback, throwableCallback);
    }
}
