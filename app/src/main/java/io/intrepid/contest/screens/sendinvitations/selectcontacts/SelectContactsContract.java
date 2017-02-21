package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.support.v7.widget.SearchView;

import java.util.List;

import io.intrepid.contest.base.BaseContract;
import io.intrepid.contest.models.Contact;

class SelectContactsContract {
    interface View extends BaseContract.View {
        boolean hasContactsPermissions();

        void displayContactList();

        void goBackToPreviousScreen();

        void updateContactList(List<Contact> contacts);

        void updateContactSearchFilter(String newFilter);

        void onContactSelected();

        void showAddContestantButton(int numContestants);

        void hideAddContestantButton();

        void showProgressBar(boolean visible);
    }

    interface Presenter extends BaseContract.Presenter<View>,
            SearchView.OnQueryTextListener, SelectContactsViewHolder.ContactClickListener {
        void onContactListUpdated(List<Contact> contacts);
    }
}
