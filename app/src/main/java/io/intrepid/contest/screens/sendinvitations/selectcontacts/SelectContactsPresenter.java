package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contact;

public class SelectContactsPresenter extends BasePresenter<SelectContactsContract.View>
        implements SelectContactsContract.Presenter {

    private static final int SEARCH_MIN_NUM_CHARACTERS = 2;
    private final List<Contact> contactList = new ArrayList<>();
    private final boolean contactSelectionEnabled;
    private int numSelectedContacts;

    public SelectContactsPresenter(@NonNull SelectContactsContract.View view,
                                   @NonNull PresenterConfiguration configuration,
                                   boolean contactSelectionEnabled,
                                   @NonNull List<Contact> contactList) {
        super(view, configuration);

        this.contactSelectionEnabled = contactSelectionEnabled;
        this.contactList.clear();
        this.contactList.addAll(contactList);
        for (Contact contact : contactList) {
            if (contact.isSelected()) {
                numSelectedContacts++;
            }
        }
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        view.setupAdapter(contactSelectionEnabled);

        if (contactList.isEmpty()) {
            view.showProgressBar(true);
            view.displayPhoneContactList();
        } else {
            view.showProgressBar(false);
            view.updateAdapterContactList(contactList);
        }
    }

    @Override
    public void onCreateOptionsMenu() {
        if (contactSelectionEnabled) {
            view.createMenuSearchItem();
        }
    }

    @Override
    public void onContactListUpdated(List<Contact> contacts) {
        List<Contact> filteredContacts = new ArrayList<>();

        for (Contact contact : contacts) {
            if (contact.getPhone().isEmpty() && contact.getEmail().isEmpty()) {
                continue;
            }

            boolean contactExists = false;
            for (Contact existingContact : contactList) {
                if (existingContact.getId() == contact.getId()) {
                    filteredContacts.add(existingContact);
                    contactExists = true;
                    break;
                }
            }

            if (!contactExists) {
                contactList.add(contact);
                filteredContacts.add(contact);

                if (contact.isSelected()) {
                    numSelectedContacts++;
                }
            }
        }

        view.updateAdapterContactList(filteredContacts);
        view.showProgressBar(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // There is no submit action (real time search)
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            view.showProgressBar(false);
            view.updateContactSearchFilter(newText);
            return true;
        }

        view.showProgressBar(true);
        if (newText.length() >= SEARCH_MIN_NUM_CHARACTERS) {
            view.updateContactSearchFilter(newText);
        }
        return true;
    }


    public void onContactClick(Contact contact) {
        if (!contactSelectionEnabled) {
            return;
        }

        boolean select = !contact.isSelected();

        contact.setSelected(select);
        view.onContactSelected();

        numSelectedContacts = select ? (numSelectedContacts + 1) : (numSelectedContacts - 1);

        if (numSelectedContacts == 0) {
            view.hideAddContestantButton();
        } else {
            view.showAddContestantButton(numSelectedContacts);
        }
    }

    @Override
    public void onAddParticipantsButtonClicked() {
        view.showSendInvitationsScreen(contactList);
    }
}
