package io.intrepid.contest.screens.contestcreation.describecontest;

import android.content.Context;
import android.support.annotation.NonNull;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.customviews.HintLabelEditText;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;
import io.intrepid.contest.screens.contestcreation.EditContestContract;

public class DescribeContestFragment extends BaseFragment<DescribeContestPresenter> implements DescribeContestContract.View, ContestCreationFragment {
    @BindView(R.id.contest_description_edittext)
    HintLabelEditText descriptionField;
    private EditContestContract activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activity = (EditContestContract) getActivity();
        } catch (ClassCastException exc) {
            throw new ClassCastException("Activity must implement " + EditContestContract.class);
        }
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

    @OnTextChanged(R.id.hint_label_edit_text)
    public final void onTextChanged(CharSequence newDescription) {
        presenter.onTextChanged(newDescription);
    }

    @Override
    public void onNextClicked() {
        presenter.onNextClicked(descriptionField.getText());
    }

    @Override
    public void saveContestDescription(String description) {
        activity = (EditContestContract) getActivity();
        activity.setContestDescription(description);
    }

    @Override
    public void setNextEnabled(boolean enabled) {
        EditContestContract activity = (EditContestContract) getActivity();
        activity.setNextEnabled(enabled);
    }
}
