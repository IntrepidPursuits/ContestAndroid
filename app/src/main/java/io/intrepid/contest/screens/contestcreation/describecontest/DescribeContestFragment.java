package io.intrepid.contest.screens.contestcreation.describecontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.customviews.HintLabelEditText;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;
import io.intrepid.contest.screens.contestcreation.EditContestContract;

public class DescribeContestFragment extends BaseFragment<DescribeContestPresenter> implements DescribeContestContract.View, ContestCreationFragment{
    @BindView(R.id.contest_description_edittext)
    EditText descriptionField;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_description;
    }

    @NonNull
    @Override
    public DescribeContestPresenter createPresenter(PresenterConfiguration configuration) {
        return new DescribeContestPresenter(this, configuration);
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
    }

    @Override
    public void onNextClicked() {
        presenter.onNextClicked(descriptionField.getText().toString());
    }

    @Override
    public void saveContestDescription(String description) {
        EditContestContract activity = (EditContestContract) getActivity();
        activity.setContestDescription(description);
    }

    @Override
    public void showError() {
        descriptionField.setError(getResources().getString(R.string.text_empty_error));
    }
}
