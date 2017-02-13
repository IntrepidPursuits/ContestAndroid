package io.intrepid.contest.screens.sendinvitations;

import io.intrepid.contest.base.BaseContract;

class SendInvitationsContract {
    interface View extends BaseContract.View {
        boolean hasContactsPermissions();

        void requestContactsPermissions();

        void showSendInvitationsMenuItem(boolean visible);

        void showSendInvitationsSkipMenuItem(boolean visible);

        void showSelectContactsButton(boolean visible);

        void showSelectContactsScreen();

        void showSelectContactsMessage();

        void showPermissionDeniedMessage();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onSelectContactsButtonClicked();

        void onContactsPermissionsResult(boolean granted);

        void onCreateOptionsMenu();

        void onOptionsItemSelected(int itemId);
    }
}
