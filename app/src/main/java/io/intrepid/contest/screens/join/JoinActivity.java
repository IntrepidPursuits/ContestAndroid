package io.intrepid.contest.screens.join;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.customviews.HintLabelEditText;
import io.intrepid.contest.screens.conteststatus.ContestStatusActivity;
import io.intrepid.contest.screens.entrysubmission.entryname.EntryNameActivity;
import io.intrepid.contest.screens.splash.SplashActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class JoinActivity extends BaseMvpActivity<JoinContract.Presenter> implements JoinContract.View {

    @BindView(R.id.enter_code_submit_button)
    Button enterCodeSubmitButton;
    @BindView(R.id.enter_code_edit_view)
    HintLabelEditText enterCodeEditView;

    public static Intent makeIntent(Context context) {
        return new Intent(context, JoinActivity.class);
    }

    @NonNull
    @Override
    public JoinContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new JoinPresenter(this, configuration);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_join;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarTitle(getResources().getString(R.string.join_contest_bar_title));
        setActionBarDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.onBackPressed();
                break;
        }
        return true;
    }

    @OnTextChanged(R.id.hint_label_edit_text)
    public void onEntryCodeTextChanged(CharSequence newCode) {
        presenter.onEntryCodeTextChanged(newCode.toString());
    }

    @OnClick(R.id.enter_code_submit_button)
    protected void onSubmitButtonClicked() {
        presenter.onSubmitButtonClicked(enterCodeEditView.getText());
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

    @Override
    public void showInvalidCodeErrorMessage() {
        String errorString = getString(R.string.error_redeeming_code);
        enterCodeEditView.setError(errorString);
    }

    @Override
    public void showContestStatusScreen() {
        startActivity(ContestStatusActivity.makeIntent(this));
    }

    @Override
    public String getLastCopiedText() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData != null && clipData.getItemAt(0) != null) {
            return clipData.getItemAt(0).getText().toString();
        }
        return null;
    }

    @Override
    public void showClipboardData(String potentialCode) {
        enterCodeEditView.setText(potentialCode);
    }

    @Override
    public void cancelJoinContest() {
        startActivity(SplashActivity.makeIntent(this));
    }
}
