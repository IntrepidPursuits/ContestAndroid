package io.intrepid.contest.screens.sendinvitations.invitationintro;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.sendinvitations.SendInvitationsActivity;
import io.intrepid.contest.screens.sendinvitations.SendInvitationsActivityContract;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class InvitationIntroFragment extends BaseFragment<InvitationIntroContract.Presenter>
        implements InvitationIntroContract.View {

    @BindView(R.id.send_invitations_icon)
    ImageView sendInvitationsIcon;
    @BindView(R.id.send_invitations_intro_text_view)
    TextView sendInvitationsIntroTextView;

    @NonNull
    @Override
    public InvitationIntroContract.Presenter createPresenter(PresenterConfiguration configuration) {
        SendInvitationsActivityContract activity = (SendInvitationsActivity) getActivity();

        return new InvitationIntroPresenter(this, configuration, activity.hasContactPermissions());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_invitation_intro;
    }

    @Override
    public void showSelectContactsMessage() {
        sendInvitationsIcon.setVisibility(VISIBLE);
        sendInvitationsIntroTextView.setText(getResources().getString(R.string.invite_contestants_intro));
    }

    @Override
    public void showPermissionDeniedMessage() {
        sendInvitationsIcon.setVisibility(GONE);
        sendInvitationsIntroTextView.setText(getString(R.string.no_contacts_permissions));

        // TODO: Enable click in part of the message that starts ACTION_PRIVACY_SETTINGS intent
        showMessage("Display permission denied message with intent to ACTION_PRIVACY_SETTINGS");
    }
}
