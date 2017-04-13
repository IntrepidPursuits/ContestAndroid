package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.support.annotation.PluralsRes;
import android.support.v7.widget.SearchView;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Contact;

class SelectContactsContract {
    interface View extends BaseContract.View {
        void createMenuSearchItem();

        void setupAdapter(boolean displayContactSelection);

        void displayPhoneContactList();

        void updateAdapterContactList(List<Contact> contacts);

        void updateContactSearchFilter(String newFilter);

        void onContactSelected();

        void showAddContactsButton(int numContacts, @PluralsRes int plural);

        void hideAddContactsButton();

        void showSendInvitationsScreen(List<Contact> contactList);

        void showProgressBar(boolean visible);
    }

    interface Presenter extends BaseContract.Presenter<View>,
            SearchView.OnQueryTextListener, SelectContactsViewHolder.ContactClickListener {
        void onCreateOptionsMenu();

        void onContactListUpdated(List<Contact> contacts);

        void onAddContactsButtonClicked();
    }
}
