package io.intrepid.contest.screens.contestcreation.namecontest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.screens.contestcreation.BaseNewContestFragment;
import io.intrepid.contest.screens.contestcreation.NewContestActivity;

public class NameContestFragment extends BaseNewContestFragment {
    @BindView(R.id.contest_name_edittext)
    EditText contestNameField;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnTextChanged(R.id.contest_name_edittext)
    public void onEntryNameTextChanged(CharSequence newName, int start, int before, int count){
        NewContestActivity activity = (NewContestActivity) getActivity();
        activity.acceptContestName(newName.toString());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_edit_contest_name;
    }

    @Override
    public boolean canMoveFurther() {
        return !TextUtils.isEmpty(contestNameField.getText());
    }

    @Override
    public int cantMoveFurtherErrorMessage() {
        return R.string.error_msg;
    }
}
