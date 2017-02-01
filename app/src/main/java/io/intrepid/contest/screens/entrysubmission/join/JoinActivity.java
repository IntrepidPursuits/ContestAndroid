package io.intrepid.contest.screens.entrysubmission.join;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.intrepid.contest.BuildConfig;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.customviews.HintLabelEditText;
import io.intrepid.contest.screens.entrysubmission.entryname.EntryNameActivity;

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
    protected int getLayoutResourceId() {
        return R.layout.activity_join;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarTitle(getResources().getString(R.string.join_contest_bar_title));
        setActionBarDisplayHomeAsUpEnabled(true);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.error_redeeming_code)
                .setTitle(R.string.error_redeeming_code_title)
                .setNeutralButton(R.string.common_ok, (dialog, id) -> {
                });
        builder.create().show();
    }

    @Override
    public void showApiErrorMessage() {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, getResources().getString(R.string.error_api), duration);
        toast.show();

        // TODO: remove lines below (skip to the next page) once API endpoint works
        if (BuildConfig.DEBUG) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(duration);
                        showEntryNameScreen();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.run();
        }
    }
}
