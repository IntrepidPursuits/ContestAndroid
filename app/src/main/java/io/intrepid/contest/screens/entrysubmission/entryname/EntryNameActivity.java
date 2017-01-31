package io.intrepid.contest.screens.entrysubmission.entryname;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.customviews.ClearableEditText;
import io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class EntryNameActivity extends BaseMvpActivity<EntryNameContract.Presenter> implements EntryNameContract.View {

    @BindView(R.id.entry_name_icon)
    ImageView entryNameIcon;
    @BindView(R.id.hint_label_edit_text)
    ClearableEditText entryNameEditText;
    @BindView(R.id.entry_name_next_button)
    Button entryNameNextButton;

    public static Intent makeIntent(Context context) {
        return new Intent(context, EntryNameActivity.class);
    }

    @NonNull
    @Override
    public EntryNameContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new EntryNamePresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_entry_name;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarTitle(R.string.contestant_welcome_message);
        setActionBarDisplayHomeAsUpEnabled(true);
    }

    @OnTextChanged(R.id.hint_label_edit_text)
    public void onEntryNameTextChanged(CharSequence newText) {
        presenter.onEntryNameTextChanged(newText.toString());
    }

    @OnClick(R.id.entry_name_next_button)
    public void onEntryNameSubmitted() {
        presenter.onEntryNameSubmitted(entryNameEditText.getText().toString());
    }

    @Override
    public void showEntryNameButton() {
        entryNameNextButton.setVisibility(VISIBLE);
    }

    @Override
    public void hideEntryNameButton() {
        entryNameNextButton.setVisibility(GONE);
    }

    @Override
    public void showEntryNameIcon() {
        entryNameIcon.setVisibility(VISIBLE);
    }

    @Override
    public void hideEntryNameIcon() {
        entryNameIcon.setVisibility(GONE);
    }

    @Override
    public void showEntryImageScreen(String entryName) {
        startActivity(EntryImageActivity.makeIntent(this, entryName));
    }
}
