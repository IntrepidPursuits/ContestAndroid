package io.intrepid.contest.screens.contestcreation.describecontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.intrepid.contest.BaseSlideFragment;
import io.intrepid.contest.R;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.screens.contestcreation.NewContestActivity;

public class DescribeContestFragment extends BaseSlideFragment<DescribeContestPresenter> implements DescribeContestContract.View {
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

    }

    @Override
    public boolean canMoveFurther() {
        return !TextUtils.isEmpty(descriptionField.getText());
    }

    @Override
    public int cantMoveFurtherErrorMessage() {
        return R.string.error_msg;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_description;
    }

    @NonNull
    @Override
    public DescribeContestPresenter createPresenter(PresenterConfiguration configuration) {
        return null;
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
}
