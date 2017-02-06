package io.intrepid.contest.screens.conteststatus.waitingsubmissions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;

public class WaitingSubmissionsFragment extends BaseFragment<WaitingSubmissionsContract.Presenter>
        implements WaitingSubmissionsContract.View {

    public static final String NUM_SUBMISSIONS_MISSING = "_num_submissions_missing_";

    @BindView(R.id.waiting_submissions_description_textview)
    TextView descriptionTextView;

    @NonNull
    @Override
    public WaitingSubmissionsPresenter createPresenter(PresenterConfiguration configuration) {
        return new WaitingSubmissionsPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_waiting_submissions;
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            updateNumSubmissionsMissing(bundle.getInt(NUM_SUBMISSIONS_MISSING));
        }
    }

    public void updateNumSubmissionsMissing(int numSubmissionsMissing) {
        presenter.onNumSubmissionsMissingUpdated(numSubmissionsMissing);
    }

    @Override
    public void showNumSubmissionsMissing(int numSubmissionsMissing) {
        String contestants = getResources()
                .getQuantityString(R.plurals.numberOfContestants, numSubmissionsMissing, numSubmissionsMissing);
        descriptionTextView.setText(
                getResources().getString(R.string.status_waiting_submissions_description, contestants));
    }
}
