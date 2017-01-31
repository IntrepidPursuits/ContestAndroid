package io.intrepid.contest.screens.entrysubmission.join;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.entrysubmission.entryname.EntryNameActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class JoinActivity extends BaseMvpActivity<JoinContract.Presenter> implements JoinContract.View {

    @BindView(R.id.enter_code_submit_button)
    Button enterCodeSubmitButton;

    public static Intent makeIntent(Context context) {
        return new Intent(context, JoinActivity.class);
    }

    @NonNull
    @Override
    public JoinContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new JoinPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_join;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarTitle(getResources().getString(R.string.join_contest_bar_title, "Chili Cookoff"));
    }

    @OnTextChanged(R.id.hint_label_edit_text)
    public void onEntryCodeTextChanged(CharSequence newCode) {
        presenter.onEntryCodeTextChanged(newCode.toString());
    }

    @OnClick(R.id.enter_code_submit_button)
    protected void onSubmitButtonClicked() {
        presenter.onSubmitButtonClicked();
    }

    @Override
    public void showSubmitButton() {
        enterCodeSubmitButton.setVisibility(VISIBLE);
    }

    @Override
    public void hideSubmitButton() {
        enterCodeSubmitButton.setVisibility(GONE);
    }

    @Override
    public void showEntryNameScreen() {
        startActivity(EntryNameActivity.makeIntent(this));
    }
}
