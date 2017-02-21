package io.intrepid.contest.screens.sendinvitations;

import java.util.List;

import io.intrepid.contest.models.Contact;
import io.intrepid.contest.models.ParticipationType;

public interface SendInvitationsActivityContract {
    boolean hasContactPermissions();

    boolean isContactSelectionEnabled();

    List<Contact> getContactList();

    void onAddContestantsButtonClicked(List<Contact> contactList);

    void onBackPressed();

    ParticipationType getParticipationType();
}
