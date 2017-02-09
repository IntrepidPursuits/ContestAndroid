package io.intrepid.contest.screens.contestcreation.namecontest;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.customviews.HintLabelEditText;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;
import io.intrepid.contest.screens.contestcreation.EditContestContract;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NameContestFragment extends BaseFragment<NameContestPresenter> implements NameContestContract.View, ContestCreationFragment {
    @BindView(R.id.contest_name_edittext)
    HintLabelEditText contestNameField;
    @BindView(R.id.trophy_icon)
    ImageView trophyIcon;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_name;
    }

    @NonNull
    @Override
    public NameContestPresenter createPresenter(PresenterConfiguration configuration) {
        Contest.Builder contestBuilder = ((EditContestContract) getActivity()).getContestBuilder();
        return new NameContestPresenter(this, configuration, contestBuilder);
    }

    @OnTextChanged(R.id.hint_label_edit_text)
    public final void onTextChanged(CharSequence newName) {
        presenter.onTextChanged(newName);
    }

    @Override
    public void onFocus() {
        onTextChanged(contestNameField.getText());
    }

    @Override
    public void setNextEnabled(boolean enabled) {
        ((EditContestContract) getActivity()).setNextEnabled(enabled);
        int trophyVisibility = enabled ? GONE : VISIBLE;
        trophyIcon.setVisibility(trophyVisibility);
    }

    @Override
    public void showNextScreen() {
        ((EditContestContract) getActivity()).showNextScreen();
    }

    @Override
    public void onNextClicked() {
        presenter.onContestTitleUpdated(contestNameField.getText());
    }
}
