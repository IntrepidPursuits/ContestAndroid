package io.intrepid.contest.screens.sendinvitations;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class SendInvitationsPresenter extends BasePresenter<SendInvitationsContract.View>
        implements SendInvitationsContract.Presenter {

    public SendInvitationsPresenter(@NonNull SendInvitationsContract.View view,
                                    @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();

        if (!view.hasContactsPermissions()) {
            view.requestContactsPermissions();
        }
    }

    @Override
    public void onSelectContactsButtonClicked() {
        view.showSelectContactsScreen();
    }

    @Override
    public void onContactsPermissionsResult(boolean granted) {
        if (granted) {
            view.showSelectContactsButton(true);
            view.showSelectContactsMessage();
        } else {
            view.showSelectContactsButton(false);
            view.showPermissionDeniedMessage();
        }
    }

    @Override
    public void onCreateOptionsMenu() {
        // TODO: Enable/disable button according to number of contacts selected
        view.showSendInvitationsMenuItem(false);
        view.showSendInvitationsSkipMenuItem(true);
    }

    @Override
    public void onOptionsItemSelected(int itemId) {
        // TODO: add menu actions
        switch (itemId) {
            case R.id.send_invitations_menu_action:
                view.showMessage("TODO: Send invitations to server");
                break;
            case R.id.send_invitations_skip_menu_action:
                view.showMessage("TODO: Go to next screen");
        }
    }
}
