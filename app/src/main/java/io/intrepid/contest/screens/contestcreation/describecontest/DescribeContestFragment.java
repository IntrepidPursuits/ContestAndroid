package io.intrepid.contest.screens.contestcreation.describecontest;

import android.support.annotation.NonNull;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.customviews.HintLabelEditText;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;
import io.intrepid.contest.screens.contestcreation.EditContestContract;

public class DescribeContestFragment extends BaseFragment<DescribeContestContract.Presenter, DescribeContestContract.View>
        implements DescribeContestContract.View, ContestCreationFragment {
    @BindView(R.id.contest_description_edittext)
    HintLabelEditText descriptionField;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_description;
    }

    @NonNull
    @Override
    public DescribeContestContract.Presenter createPresenter(PresenterConfiguration configuration) {
        Contest.Builder contestBuilder = ((EditContestContract) getActivity()).getContestBuilder();
        return new DescribeContestPresenter(this, configuration, contestBuilder);
    }

    @Override
    public boolean isNextPageButtonEnabled() {
        return getPresenter().isNextPageButtonEnabled();
    }

    @Override
    public void onNextPageButtonClicked() {
        getPresenter().onNextPageButtonClicked(descriptionField.getText());
    }

    @Override
    public void setNextEnabled(boolean enabled) {
        ((EditContestContract) getActivity()).onNextPageEnabledChanged();
    }

    @Override
    public void showNextScreen() {
        ((EditContestContract) getActivity()).showNextScreen();
    }
}
