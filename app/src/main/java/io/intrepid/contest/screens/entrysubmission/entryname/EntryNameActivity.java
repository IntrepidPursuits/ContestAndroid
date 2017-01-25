package io.intrepid.contest.screens.entrysubmission.entryname;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.customviews.ClearableEditText;
import timber.log.Timber;

import static io.intrepid.contest.screens.entrysubmission.entryname.EntryNameContract.Presenter;
import static io.intrepid.contest.screens.entrysubmission.entryname.EntryNameContract.View;

public class EntryNameActivity extends BaseMvpActivity<Presenter> implements View {

    @BindView(R.id.contestant_welcome_text_view)
    TextView contestantWelcomeTextView;
    @BindView(R.id.entry_name_edit_text)
    ClearableEditText entryNameEditText;
    @BindView(R.id.entry_name_next_button)
    Button entryNameNextButton;

    public static Intent makeIntent(Context context) {
        return new Intent(context, EntryNameActivity.class);
    }

    @NonNull
    @Override
    public Presenter createPresenter(PresenterConfiguration configuration) {
        return new EntryNamePresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_entry_name;
    }

    @OnFocusChange(R.id.entry_name_edit_text)
    public void onEntryNameFocusChanged(boolean isFocused) {
        presenter.onEntryNameFocusChanged(isFocused);
    }

    @OnTextChanged(R.id.entry_name_edit_text)
    public void onEntryNameTextChanged(CharSequence newText, int start, int before, int count) {
        presenter.onEntryNameTextChanged(newText.toString());
    }

    @OnClick(R.id.entry_name_next_button)
    public void onEntryNameSubmitted() {
        presenter.onEntryNameSubmitted(entryNameEditText.getText().toString());
    }

    @Override
    public void setContestName(String contestName) {
        contestantWelcomeTextView.setText(getResources().getString(R.string.contestant_welcome_message, contestName));
    }

    @Override
    public void showEntryNameButton() {
        entryNameNextButton.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void hideEntryNameButton() {
        entryNameNextButton.setVisibility(android.view.View.GONE);
    }

    @Override
    public void enableEntryNameButton() {
        entryNameNextButton.setEnabled(true);
    }

    @Override
    public void disableEntryNameButton() {
        entryNameNextButton.setEnabled(false);
    }

    @Override
    public void showEntryImageScreen(String entryName) {
        Timber.d("Start entry image activity, sending entry name");
    }
}
