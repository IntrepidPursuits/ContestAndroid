package io.intrepid.contest.screens.entrysubmission.entryname;

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
import io.intrepid.contest.customviews.ClearableEditText;
import io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageActivity;

import static io.intrepid.contest.screens.entrysubmission.entryname.EntryNameContract.Presenter;
import static io.intrepid.contest.screens.entrysubmission.entryname.EntryNameContract.View;

public class EntryNameActivity extends BaseMvpActivity<Presenter> implements View {

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

    @OnTextChanged(R.id.entry_name_edit_text)
    public void onEntryNameTextChanged(CharSequence newText) {
        presenter.onEntryNameTextChanged(newText.toString());
    }

    @OnClick(R.id.entry_name_next_button)
    public void onEntryNameSubmitted() {
        presenter.onEntryNameSubmitted(entryNameEditText.getText().toString());
    }

    @Override
    public void showWelcomeMessage() {
        setActionBarTitle(getResources().getString(R.string.contestant_welcome_message));
    }

    @Override
    public void enableEntryNameButton() {
        entryNameNextButton.setEnabled(true);
        entryNameNextButton.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void disableEntryNameButton() {
        entryNameNextButton.setEnabled(false);
        entryNameNextButton.setVisibility(android.view.View.GONE);
    }

    @Override
    public void showEntryImageScreen(String entryName) {
        startActivity(EntryImageActivity.makeIntent(this, entryName));
    }
}
