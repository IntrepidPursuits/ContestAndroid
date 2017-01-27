package io.intrepid.contest.screens.entrysubmission.join;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.entrysubmission.entryname.EntryNameActivity;
import io.intrepid.contest.screens.entrysubmission.join.JoinContract.Presenter;

public class JoinActivity extends BaseMvpActivity<Presenter> implements JoinContract.View {

    @BindView(R.id.enter_code_submit_button)
    Button enterCodeSubmitButton;

    public static Intent makeIntent(Context context) {
        return new Intent(context, JoinActivity.class);
    }

    @NonNull
    @Override
    public Presenter createPresenter(PresenterConfiguration configuration) {
        return new JoinPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_join;
    }

    @OnTextChanged(R.id.enter_code_text_view)
    public void onEntryCodeTextChanged(CharSequence newCode) {
        presenter.onEntryCodeTextChanged(newCode.toString());
    }

    @OnClick(R.id.enter_code_submit_button)
    protected void onSubmitButtonClicked() {
        presenter.onSubmitButtonClicked();
    }

    @Override
    public void enableSubmitButton() {
        enterCodeSubmitButton.setEnabled(true);
    }

    @Override
    public void disableSubmitButton() {
        enterCodeSubmitButton.setEnabled(false);
    }

    @Override
    public void showEntryNameScreen() {
        startActivity(EntryNameActivity.makeIntent(this));
    }
}
