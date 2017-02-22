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

public class SendInvitationsPresenter extends BasePresenter<SendInvitationsContract.View>
        implements SendInvitationsContract.Presenter {

    private final List<Contact> fullContactList = new ArrayList<>();
    private final List<Contact> selectedContactList = new ArrayList<>();
    private boolean contactSelectionEnabled = false;
    private boolean displayMenuItemsAndActionBar = true;
    private ParticipationType participationType;

    public SendInvitationsPresenter(@NonNull SendInvitationsContract.View view,
                                    @NonNull PresenterConfiguration configuration) {
        super(view, configuration);

        this.participationType = ParticipationType.CONTESTANT;
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
        return view.checkContactsPermissions();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        showPreviewContent();
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();

        if (!hasContactPermissions()) {
            view.requestContactsPermissions();
            showPreviewContent();
        }
    }

    @Override
    public void onSelectContactsButtonClicked() {
        showSelectContactsContent();
    }

    private void showSelectContactsContent() {
        displayMenuItemsAndActionBar = false;

        view.showSelectContactsButton(false);

        setContactSelectionEnabled(true);
        view.showSelectContactsFragment();
    }

    @Override
    public void onAddContestantsButtonClicked(List<Contact> contactList) {
        fullContactList.clear();
        fullContactList.addAll(contactList);

        selectedContactList.clear();
        for (Contact contact : contactList) {
            if (contact.isSelected()) {
                selectedContactList.add(contact);
            }
        }

        showPreviewContent();
    }

    private void showPreviewContent() {
        displayMenuItemsAndActionBar = true;
        boolean hasPermissions = hasContactPermissions();
        view.showSelectContactsButton(hasPermissions);

        if (!hasPermissions || selectedContactList.isEmpty()) {
            view.showInvitationIntroFragment();
        } else {
            setContactSelectionEnabled(false);
            view.showSelectContactsFragment();
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
                // TODO: go to next screen
                view.showMessage("TODO: Go to next screen");
            case android.R.id.home:
                onBackButtonClicked();
        }
    }

    private void sendInvitations() {
        Timber.d("Batch invitation API call.");

        if (selectedContactList.isEmpty()) {
            view.showMessage(R.string.no_contacts_selected);
            return;
        }

        String contestId = persistentSettings.getCurrentContestId().toString();
        BatchInviteRequest batchInviteRequest = new BatchInviteRequest();
        batchInviteRequest.invitations = new InvitationRequest(contestId, participationType, selectedContactList);

        Disposable disposable = restApi.batchInvite(contestId, batchInviteRequest)
                .compose(subscribeOnIoObserveOnUi())
                .subscribe(response -> showResult(response), throwable -> {
                    Timber.d("API error sending batch invitations: " + throwable.getMessage());
                    view.showMessage(R.string.error_api);
                });
        disposables.add(disposable);
    }

    private void showResult(BatchInviteResponse response) {
        view.showMessage("Invitations sent!");
        for (InvitationResponse invitationResponse : response.invitationResponses) {
            Timber.d("Code: " + invitationResponse.code);
        }
        view.showMessage("Go to next screen");
    }

    private void updateMenuItemsAndActionBar() {
        if (displayMenuItemsAndActionBar) {
            view.setActionBarTitle(R.string.invite_contestants_bar_title);
            view.setActionBarDisplayHomeAsUpEnabled(false);
            view.showSendInvitationsMenuItem(!selectedContactList.isEmpty());
            view.showSendInvitationsSkipMenuItem(selectedContactList.isEmpty());
        } else {
            view.showSendInvitationsMenuItem(false);
            view.showSendInvitationsSkipMenuItem(false);
        }
    }

    @Override
    public void onBackButtonClicked() {
        if (contactSelectionEnabled) {
            showPreviewContent();
        }
    }
}
