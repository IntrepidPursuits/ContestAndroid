package io.intrepid.contest.screens.adminstatus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import io.intrepid.contest.R;

import static io.intrepid.contest.screens.adminstatus.ConfirmStartContestDialog.AdminActionType.END_CONTEST;
import static io.intrepid.contest.screens.adminstatus.ConfirmStartContestDialog.AdminActionType.START_CONTEST;

public class ConfirmStartContestDialog extends DialogFragment {
    private static final String DIALOG_ACTION = "Action";
    private DialogInteractionListener listener;
    private AdminActionType action;
    @StringRes
    private int title;
    @StringRes
    private int message;

    public static ConfirmStartContestDialog newInstance(AdminActionType action, DialogInteractionListener listener) {
        Bundle args = new Bundle();
        args.putString(DIALOG_ACTION, action.name());
        ConfirmStartContestDialog fragment = new ConfirmStartContestDialog();
        fragment.setListener(listener);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(DialogInteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            String adminActionString = args.getString(DIALOG_ACTION, START_CONTEST.toString());
            action = adminActionString.equals(START_CONTEST.toString()) ? START_CONTEST : END_CONTEST;
            if (action == START_CONTEST) {
                title = R.string.not_all_contestants_received_prompt;
                message = R.string.start_contest_prompt;
            } else {
                title = R.string.not_all_judge_scores_received_prompt;
                message = R.string.end_contest_prompt;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
            int textColor = R.color.colorPrimary;
            positiveButton.setTextColor(ContextCompat.getColor(getActivity(), textColor));
            negativeButton.setTextColor(ContextCompat.getColor(getActivity(), textColor));
            positiveButton.setAllCaps(false);
            negativeButton.setAllCaps(false);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context ctx = getActivity();
        return new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.common_yes, (dialog, which) -> listener.onPositiveButtonClicked(action))
                .setNegativeButton(R.string.common_no, (dialog, which) -> listener.onNegativeButtonClicked(action))
                .create();
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, ConfirmStartContestDialog.class.getSimpleName());
    }

    public enum AdminActionType {
        START_CONTEST,
        END_CONTEST;

        @Override
        public String toString() {
            return name();
        }
    }

    public interface DialogInteractionListener {
        void onPositiveButtonClicked(AdminActionType actionType);

        void onNegativeButtonClicked(AdminActionType actionType);
    }
}
