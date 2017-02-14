package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contact;

public class SelectContactsPresenter extends BasePresenter<SelectContactsContract.View>
        implements SelectContactsContract.Presenter {

    public SelectContactsPresenter(@NonNull SelectContactsContract.View view,
                                   @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();

        if (view.hasContactsPermissions()) {
            view.displayContactList();
        } else {
            view.goBackToPreviousScreen();
        }
    }

    @Override
    public void onContactListUpdated(List<Contact> contacts) {
        List<Contact> filteredContacts = new ArrayList<>();
        for (Contact contact : contacts) {
            if (!contact.getPhone().isEmpty() || !contact.getEmail().isEmpty()) {
                filteredContacts.add(contact);
            }
        }

        view.updateContactList(filteredContacts);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // There is no submit action (real time search)
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        view.updateContactSearchFilter(newText);
        return true;
    }
}
