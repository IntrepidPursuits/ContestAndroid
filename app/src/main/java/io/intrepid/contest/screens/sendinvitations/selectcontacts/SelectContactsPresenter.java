package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contact;
import io.intrepid.contest.models.ParticipationType;

public class SelectContactsPresenter extends BasePresenter<SelectContactsContract.View>
        implements SelectContactsContract.Presenter {

    private static final int SEARCH_MIN_NUM_CHARACTERS = 2;
    private final ParticipationType participationType;
    private final boolean contactSelectionEnabled;
    private final List<Contact> contactList = new ArrayList<>();
    private int numSelectedContacts;

    public SelectContactsPresenter(@NonNull SelectContactsContract.View view,
                                   @NonNull PresenterConfiguration configuration,
                                   ParticipationType participationType,
                                   boolean contactSelectionEnabled,
                                   @NonNull List<Contact> contactList) {
        super(view, configuration);

        this.participationType = participationType;
        this.contactSelectionEnabled = contactSelectionEnabled;
        this.contactList.clear();
        this.contactList.addAll(contactList);
        for (Contact contact : contactList) {
            if (contact.isEnabled() && contact.isSelected()) {
                numSelectedContacts++;
            }
        }
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        getView().setupAdapter(contactSelectionEnabled);

        if (contactList.isEmpty()) {
            getView().showProgressBar(true);
            getView().displayPhoneContactList();
        } else {
            getView().showProgressBar(false);
            getView().updateAdapterContactList(contactList);
        }
    }

    @Override
    public void onCreateOptionsMenu() {
        if (contactSelectionEnabled) {
            getView().createMenuSearchItem();
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

                if (contact.isEnabled() && contact.isSelected()) {
                    numSelectedContacts++;
                }
            }
        }

        getView().updateAdapterContactList(filteredContacts);
        getView().showProgressBar(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // There is no submit action (real time search)
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            getView().showProgressBar(false);
            getView().updateContactSearchFilter(newText);
            return true;
        }

        getView().showProgressBar(true);
        if (newText.length() >= SEARCH_MIN_NUM_CHARACTERS) {
            getView().updateContactSearchFilter(newText);
        }
        return true;
    }


    public void onContactClick(Contact contact) {
        if (!contactSelectionEnabled || !contact.isEnabled()) {
            return;
        }

        boolean select = !contact.isSelected();

        contact.setSelected(select);
        getView().onContactSelected();

        numSelectedContacts = select ? (numSelectedContacts + 1) : (numSelectedContacts - 1);

        if (numSelectedContacts == 0) {
            getView().hideAddContactsButton();
        } else {
            int pluralResource = participationType.equals(ParticipationType.CONTESTANT) ?
                    R.plurals.numberOfContestants : R.plurals.numberOfJudges;
            getView().showAddContactsButton(numSelectedContacts,
                                            pluralResource);
        }
    }

    @Override
    public void onAddContactsButtonClicked() {
        getView().showSendInvitationsScreen(contactList);
    }
}
