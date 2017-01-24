package io.intrepid.contest.screens.contestcreation.namecontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.customviews.HintLabelEditText;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_name;
    }

    @OnTextChanged(R.id.hint_label_edit_text)
    public final void onTextChanged(CharSequence newName) {
        if(TextUtils.isEmpty(newName)){
            trophyIcon.setVisibility(VISIBLE);
        }else{
            trophyIcon.setVisibility(GONE);
        }
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
    public void saveEnteredName(String contestName){
        EditContestContract activity = (EditContestContract) getActivity();
        activity.setContestName(contestName);
    }

    @Override
    public void showError() {
        contestNameField.setError(getString(R.string.text_empty_error));
    }
}
