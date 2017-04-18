package io.intrepid.contest.screens.sendinvitations;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contact;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.BatchInviteRequest;
import io.intrepid.contest.rest.BatchInviteResponse;
import io.intrepid.contest.rest.InvitationRequest;
import io.intrepid.contest.rest.InvitationResponse;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

class SendInvitationsPresenter extends BasePresenter<SendInvitationsContract.View>
        implements SendInvitationsContract.Presenter {

    private final List<Contact> fullContactList = new ArrayList<>();
    private final List<Contact> selectedContactList = new ArrayList<>();
    private SendInvitationsContent lastShowed;
    private boolean contactSelectionEnabled = false;
    private boolean displayMenuItemsAndActionBar = true;
    private ParticipationType invitationParticipantType;

    SendInvitationsPresenter(@NonNull SendInvitationsContract.View view,
                             @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    public boolean isContactSelectionEnabled() {
        return contactSelectionEnabled;
    }

    private void setContactSelectionEnabled(boolean enabled) {
        this.contactSelectionEnabled = enabled;
    }

    @Override
    public List<Contact> getContactList() {
        if (isContactSelectionEnabled()) {
            return fullContactList;
        }
        return selectedContactList;
    }

    @Override
    public boolean hasContactPermissions() {
        return getView().checkContactsPermissions();
    }

    public ParticipationType getInvitationParticipantType() {
        return invitationParticipantType;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        invitationParticipantType = ParticipationType.CONTESTANT;
        showPreviewContent();
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();

        if (!hasContactPermissions()) {
            getView().requestContactsPermissions();
        }

        // Restore content
        if (lastShowed == SendInvitationsContent.SELECT_CONTACTS) {
            showSelectContactsContent();
        } else {
            showPreviewContent();
        }
    }

    @Override
    public void onSelectContactsButtonClicked() {
        showSelectContactsContent();
    }

    private void showSelectContactsContent() {
        lastShowed = SendInvitationsContent.SELECT_CONTACTS;

        displayMenuItemsAndActionBar = false;
        getView().showSelectContactsButton(false);
        setContactSelectionEnabled(true);
        getView().showSelectContactsFragment();
    }

    @Override
    public void onAddContestantsButtonClicked(List<Contact> contactList) {
        fullContactList.clear();
        fullContactList.addAll(contactList);

        selectedContactList.clear();
        for (Contact contact : contactList) {
            if (contact.isEnabled() && contact.isSelected()) {
                selectedContactList.add(contact);
            }
        }

        showPreviewContent();
    }

    private void showPreviewContent() {
        displayMenuItemsAndActionBar = true;
        boolean hasPermissions = hasContactPermissions();
        getView().showSelectContactsButton(hasPermissions);

        if (!hasPermissions || selectedContactList.isEmpty()) {
            lastShowed = SendInvitationsContent.PREVIEW_INTRO;
            getView().showInvitationIntroFragment();
        } else {
            lastShowed = SendInvitationsContent.PREVIEW_CONTACTS;
            setContactSelectionEnabled(false);
            getView().showSelectContactsFragment();
        }
    }

    @Override
    public void onContactsPermissionsResult() {
        showPreviewContent();
    }

    @Override
    public void onCreateOptionsMenu() {
        updateMenuItemsAndActionBar();
    }

    @Override
    public void onOptionsItemSelected(int itemId) {
        switch (itemId) {
            case R.id.send_invitations_menu_action:
                sendInvitations();
                break;
            case R.id.send_invitations_skip_menu_action:
                showNextScreen();
                break;
            case android.R.id.home:
                onBackButtonClicked();
                break;
        }
    }

    private void showNextScreen() {
        // Screen to display after inviting participants of this type
        switch (invitationParticipantType) {
            case CONTESTANT:
                invitationParticipantType = ParticipationType.JUDGE;
                selectedContactList.clear();
                showPreviewContent();
                updateMenuItemsAndActionBar();
                break;
            case JUDGE:
                getView().showContestStatusScreen();
        }
    }

    private void sendInvitations() {
        Timber.d("Batch invitation API call.");

        if (selectedContactList.isEmpty()) {
            getView().showMessage(R.string.no_contacts_selected);
            return;
        }

        String contestId = getPersistentSettings().getCurrentContestId().toString();
        BatchInviteRequest batchInviteRequest = new BatchInviteRequest();
        batchInviteRequest.invitations = new InvitationRequest(contestId,
                                                               invitationParticipantType, selectedContactList);

        Disposable disposable = getRestApi().batchInvite(contestId, batchInviteRequest)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> showResult(response), throwable -> {
                    Timber.d("API error sending batch invitations: " + throwable.getMessage());
                    getView().showMessage(R.string.error_api);
                });
        getDisposables().add(disposable);
    }

    private void showResult(BatchInviteResponse response) {
        getView().showMessage("Invitations sent!");
        for (InvitationResponse invitationResponse : response.invitationResponses) {
            Timber.d("Code: " + invitationResponse.code);
        }
        disableContactsForFutureSelection();
        showNextScreen();
    }

    private void disableContactsForFutureSelection() {
        for (Contact contact : selectedContactList) {
            contact.setEnabled(false);
        }
    }

    private void updateMenuItemsAndActionBar() {
        if (displayMenuItemsAndActionBar) {
            getView().setActionBarTitle(invitationParticipantType.equals(ParticipationType.CONTESTANT) ?
                                           R.string.invite_contestants_bar_title : R.string.invite_judges_bar_title);
            getView().setActionBarDisplayHomeAsUpEnabled(false);
            getView().showSendInvitationsMenuItem(!selectedContactList.isEmpty());
            getView().showSendInvitationsSkipMenuItem(selectedContactList.isEmpty());
        } else {
            getView().showSendInvitationsMenuItem(false);
            getView().showSendInvitationsSkipMenuItem(false);
        }
    }

    @Override
    public void onBackButtonClicked() {
        if (contactSelectionEnabled) {
            showPreviewContent();
        } else {
            getView().cancelSelection();
        }
    }

    private enum SendInvitationsContent {
        PREVIEW_INTRO, PREVIEW_CONTACTS, SELECT_CONTACTS
    }
}
