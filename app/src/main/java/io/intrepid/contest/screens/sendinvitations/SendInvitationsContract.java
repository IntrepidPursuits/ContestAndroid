package io.intrepid.contest.screens.sendinvitations;

import android.support.annotation.StringRes;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Contact;
import io.intrepid.contest.models.ParticipationType;

class SendInvitationsContract {
    interface View extends BaseContract.View {
        void setActionBarTitle(@StringRes int titleResource);

        void setActionBarDisplayHomeAsUpEnabled(boolean enabled);

        boolean checkContactsPermissions();

        void requestContactsPermissions();

        void showSendInvitationsMenuItem(boolean visible);

        void showSendInvitationsSkipMenuItem(boolean visible);

        void showSelectContactsButton(boolean visible);

        void showSelectContactsFragment();

        void showInvitationIntroFragment();

        void showContestStatusScreen();

        void cancelSelection();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void onBackButtonClicked();

        void onSelectContactsButtonClicked();

        void onAddContestantsButtonClicked(List<Contact> selectedContactList);

        void onContactsPermissionsResult();

        void onCreateOptionsMenu();

        void onOptionsItemSelected(int itemId);

        boolean isContactSelectionEnabled();

        List<Contact> getContactList();

        boolean hasContactPermissions();

        ParticipationType getInvitationParticipantType();
    }
}
