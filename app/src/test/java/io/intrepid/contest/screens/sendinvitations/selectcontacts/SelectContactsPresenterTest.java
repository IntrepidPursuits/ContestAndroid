package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import io.intrepid.contest.models.Contact;
import io.intrepid.contest.testutils.BasePresenterTest;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SelectContactsPresenterTest extends BasePresenterTest<SelectContactsPresenter> {
    @Mock
    SelectContactsContract.View mockView;

    @Before
    public void setup() {
        presenter = new SelectContactsPresenter(mockView, testConfiguration);
    }

    @Test
    public void onViewBoundShouldCheckForContactsPermissions() {
        presenter.onViewBound();
        verify(mockView).hasContactsPermissions();
    }

    @Test
    public void onViewBoundShouldDisplayContactListWhenAppHasPermissions() {
        when(mockView.hasContactsPermissions()).thenReturn(true);
        presenter.onViewBound();
        verify(mockView).displayContactList();
    }

    @Test
    public void onViewBoundShouldGoBackToPreviousScreenWhenAppDoesNotHavePermissions() {
        when(mockView.hasContactsPermissions()).thenReturn(false);
        presenter.onViewBound();
        verify(mockView).goBackToPreviousScreen();
    }

    @Test
    public void onContactListUpdatedShouldUpdateContactListFilteringOutContactsWithoutPhoneOrEmail() {
        ArrayList<Contact> initialContactList = getMockContactList();
        presenter.onContactListUpdated(initialContactList);
        verify(mockView).updateContactList(argThat(argument -> argument.size() == initialContactList.size() - 1));
    }

    private ArrayList<Contact> getMockContactList() {
        final String EMPTY = "";
        final String TEST_PHONE = "555-555-5555";
        final String TEST_EMAIL = "email@test.com";
        ArrayList<Contact> contactList = new ArrayList<>();

        Contact noPhoneOrEmail = new Contact();
        noPhoneOrEmail.setPhone(EMPTY);
        noPhoneOrEmail.setEmail(EMPTY);
        contactList.add(noPhoneOrEmail);

        Contact onlyPhone = new Contact();
        onlyPhone.setPhone(TEST_PHONE);
        onlyPhone.setEmail(EMPTY);
        contactList.add(onlyPhone);

        Contact onlyEmail = new Contact();
        onlyEmail.setPhone(EMPTY);
        onlyEmail.setEmail(TEST_EMAIL);
        contactList.add(onlyEmail);

        Contact phoneAndEmail = new Contact();
        phoneAndEmail.setPhone(TEST_PHONE);
        phoneAndEmail.setEmail(TEST_EMAIL);
        contactList.add(phoneAndEmail);

        return contactList;
    }

    @Test
    public void onQueryTextSubmitShouldAlwaysReturnTrue() {
        assertTrue(presenter.onQueryTextSubmit(""));
    }

    @Test
    public void onQueryTextChangeShouldUpdateContactSearchFilter() {
        presenter.onQueryTextChange(anyString());
        verify(mockView).updateContactSearchFilter(anyString());
    }
}
