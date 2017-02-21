package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contact;

public class SelectContactsPresenter extends BasePresenter<SelectContactsContract.View>
        implements SelectContactsContract.Presenter {

    public static final int SEARCH_MIN_NUM_CHARACTERS = 2;
    private final List<Contact> filteredContacts = new ArrayList<>();
    private int numSelectedContacts;

    public SelectContactsPresenter(@NonNull SelectContactsContract.View view,
                                   @NonNull PresenterConfiguration configuration) {
        super(view, configuration);

        numSelectedContacts = 0;
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();

        if (!view.hasContactsPermissions()) {
            view.goBackToPreviousScreen();
            return;
        }

        view.showProgressBar(true);
        view.displayContactList();
    }

    private void updateAddContestantButton() {
        if (numSelectedContacts == 0) {
            view.hideAddContestantButton();
        } else {
            view.showAddContestantButton(numSelectedContacts);
        }
    }

    @Override
    public void onContactListUpdated(List<Contact> contacts) {
        numSelectedContacts = 0;
        filteredContacts.clear();
        for (Contact contact : contacts) {
            if (!contact.getPhone().isEmpty() || !contact.getEmail().isEmpty()) {
                filteredContacts.add(contact);
                if (contact.isSelected()) {
                    numSelectedContacts++;
                }
            }
        }

        view.updateContactList(filteredContacts);
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
        boolean select = !contact.isSelected();

        contact.setSelected(select);
        view.onContactSelected();

        numSelectedContacts = select ? numSelectedContacts + 1 : numSelectedContacts - 1;
        updateAddContestantButton();
    }
}
