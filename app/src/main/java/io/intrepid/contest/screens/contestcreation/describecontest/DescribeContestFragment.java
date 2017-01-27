package io.intrepid.contest.screens.contestcreation.describecontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseViewPagerFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.base.TextValidatableView;
import io.intrepid.contest.models.Category;

public class DescribeContestFragment extends BaseViewPagerFragment<DescribeContestPresenter> implements DescribeContestContract.View, TextValidatableView {
    private static final String CONTEST_NAME = "ContestName";
    @BindView(R.id.contest_description_edittext)
    EditText descriptionField;

    public static DescribeContestFragment newInstance(String contestName) {
        Bundle args = new Bundle();
        DescribeContestFragment fragment = new DescribeContestFragment();
        args.putString(CONTEST_NAME, contestName);
        fragment.setArguments(args);
        return fragment;
    }

    @OnTextChanged(R.id.contest_description_edittext)
    public void onEntryNameTextChanged(CharSequence description, int start, int before, int count) {
        presenter.submitContestDescription(String.valueOf(description));
    }

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
        descriptionField.setHint(R.string.description_optional);
    }

    @Override
    public void onContestDescriptionDone(Category category) {

    }

    @Override
    public void onCancelClicked() {

    }

    @Override
    public boolean areAllFieldValid() {
        return true;
    }

    @Override
    public int errorMessage() {
        return 0;
    }

    @Override
    public void submitText() {
        presenter.submitContestDescription(descriptionField.getText().toString());
    }
}
