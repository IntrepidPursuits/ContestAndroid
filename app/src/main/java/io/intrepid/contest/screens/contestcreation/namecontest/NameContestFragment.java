package io.intrepid.contest.screens.contestcreation.namecontest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.intrepid.contest.BaseSlideFragment;
import io.intrepid.contest.R;
import io.intrepid.contest.base.PresenterConfiguration;

public class NameContestFragment extends BaseSlideFragment<NameContestPresenter> implements NameContestContract.View {
    @BindView(R.id.contest_name_edittext)
    EditText contestNameField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnTextChanged(R.id.contest_name_edittext)
    public void onEntryNameTextChanged(CharSequence newName, int start, int before, int count){

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
    public boolean canMoveFurther() {
        return !TextUtils.isEmpty(contestNameField.getText());
    }

    @Override
    public int cantMoveFurtherErrorMessage() {
        return R.string.error_msg;
    }

    @Override
    public void acceptContestDescription(String contestDescription) {

    }
}
