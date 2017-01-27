package io.intrepid.contest.screens.contestcreation.namecontest;

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

public class NameContestFragment extends BaseViewPagerFragment<NameContestPresenter> implements NameContestContract.View, TextValidatableView {
    @BindView(R.id.contest_name_edittext)
    EditText contestNameField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnTextChanged(R.id.contest_name_edittext)
    public void onEntryNameTextChanged(CharSequence newName, int start, int before, int count){
        onContestNameEntered(String.valueOf(newName));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_name;
    }

    @NonNull
    @Override
    public NameContestPresenter createPresenter(PresenterConfiguration configuration) {
        return new NameContestPresenter(this, configuration);
    }

    @Override
    public void onContestNameEntered(String contestName) {
        presenter.updateContestName(contestName);
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
        presenter.updateContestName(contestNameField.getText().toString());
    }
}
