package io.intrepid.contest.screens.sendinvitations;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contact;

public class SendInvitationsPresenter extends BasePresenter<SendInvitationsContract.View>
        implements SendInvitationsContract.Presenter {

    private final List<Contact> fullContactList = new ArrayList<>();
    private final List<Contact> selectedContactList = new ArrayList<>();
    private boolean contactSelectionEnabled = false;
    private boolean displayMenuItems = true;

    public SendInvitationsPresenter(@NonNull SendInvitationsContract.View view,
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
        displayMenuItems = false;

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
        displayMenuItems = true;
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
        displayMenuItems();
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

    private void displayMenuItems() {
        if (displayMenuItems) {
            view.setActionBarTitle(R.string.invite_contestants_bar_title);
            view.showSendInvitationsMenuItem(!selectedContactList.isEmpty());
            view.showSendInvitationsSkipMenuItem(selectedContactList.isEmpty());
        } else {
            view.showSendInvitationsMenuItem(false);
            view.showSendInvitationsSkipMenuItem(false);
        }
    }
}
