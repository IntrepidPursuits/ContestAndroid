package io.intrepid.contest.screens.contestcreation.namecontest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoriesFragment;
import io.intrepid.contest.screens.contestcreation.addcategoriestocontest.AddCategoryActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NameContestFragment extends BaseFragment<NameContestPresenter> implements NameContestContract.View, ContestCreationFragment {
    @BindView(R.id.contest_name_edittext)
    HintLabelEditText contestNameField;
    @BindView(R.id.trophy_icon)
    ImageView trophyIcon;
    EditContestContract contestEditorActivity;

    public static NameContestFragment newInstance(Contest.Builder contest) {
        Bundle args = new Bundle();
        args.putParcelable(AddCategoryActivity.CONTEST_KEY, contest);
        NameContestFragment fragment = new NameContestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_name;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contestEditorActivity = (EditContestContract) getActivity();
    }

    @OnTextChanged(R.id.hint_label_edit_text)
    public final void onTextChanged(CharSequence newName) {
        presenter.onTextChanged(newName);
    }

    @Override
    public void setNextEnabled(boolean enabled) {
        contestEditorActivity.setNextEnabled(enabled);
        int trophyVisibility = enabled ? GONE : VISIBLE;
        trophyIcon.setVisibility(trophyVisibility);
    }

    @NonNull
    @Override
    public NameContestPresenter createPresenter(PresenterConfiguration configuration) {
        return new NameContestPresenter(this, configuration);
    }

    @Override
    public void onNextClicked() {
        presenter.onContestNameUpdate(contestNameField.getText().toString());
    }

    @Override
    public void saveEnteredName(String contestName) {
        contestEditorActivity.setContestName(contestName);
    }
}
