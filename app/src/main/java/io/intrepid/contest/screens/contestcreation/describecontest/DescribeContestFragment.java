package io.intrepid.contest.screens.contestcreation.describecontest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.screens.contestcreation.BaseNewContestFragment;
import io.intrepid.contest.screens.contestcreation.NewContestActivity;

public class DescribeContestFragment extends BaseNewContestFragment {
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
        NewContestActivity activity = (NewContestActivity) getActivity();
        activity.acceptContestDescription(description.toString());
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        descriptionField.setHint(R.string.description_optional);
    }
}
