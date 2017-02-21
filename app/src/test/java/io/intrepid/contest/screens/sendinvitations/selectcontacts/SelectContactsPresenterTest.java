package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Contact;
import io.intrepid.contest.testutils.BasePresenterTest;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
    public void onViewBoundShouldShowProgressBarWhenAppHasPermissions() {
        when(mockView.hasContactsPermissions()).thenReturn(true);
        presenter.onViewBound();
        verify(mockView).showProgressBar(true);
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
        List<Contact> initialContactList = getMockContactList();
        presenter.onContactListUpdated(initialContactList);
        verify(mockView).updateContactList(argThat(argument -> argument.size() == initialContactList.size() - 1));
    }

    @Test
    public void onContactListUpdatedShouldHideProgressBar() {
        presenter.onContactListUpdated(getMockContactList());
        verify(mockView).showProgressBar(false);
    }

    @Test
    public void onQueryTextSubmitShouldAlwaysReturnTrue() {
        assertTrue(presenter.onQueryTextSubmit(""));
    }

    @Test
    public void onQueryTextChangeShouldUpdateContactSearchFilterWhenTextIsEmpty() {
        presenter.onQueryTextChange("");
        verify(mockView).updateContactSearchFilter(anyString());
    }

    @Test
    public void onQueryTextChangeShouldNotUpdateContactSearchFilterWhenTextHasOneCharacter() {
        presenter.onQueryTextChange("1");
        verify(mockView, never()).updateContactSearchFilter(anyString());
    }

    @Test
    public void onQueryTextChangeShouldUpdateContactSearchFilterWhenTextHasTwoCharactersOrMore() {
        presenter.onQueryTextChange("12");
        verify(mockView).updateContactSearchFilter(anyString());
    }

    @Test
    public void onQueryTextChangeShouldHideProgressBarWhenTextIsEmpty() {
        presenter.onQueryTextChange("");
        verify(mockView).showProgressBar(false);
    }

    @Test
    public void onQueryTextChangeShouldShowProgressBarWhenTextIsNotEmpty() {
        presenter.onQueryTextChange("1");
        verify(mockView).showProgressBar(true);
    }

    @Test
    public void onContactClickShouldNotifyAdapterDataSetChangedWhenContactIsClicked() {
        presenter.onContactListUpdated(getMockContactList()); // Pre-requisite

        presenter.onContactClick(mock(Contact.class));

        verify(mockView).onContactSelected();
    }

    @Test
    public void onContactClickShouldShowAndIncreaseAddContestantButtonWhenUnselectedContactIsClicked() {
        List<Contact> list = getMockContactList();
        list.get(0).setSelected(false);
        presenter.onContactListUpdated(list);

        presenter.onContactClick(list.get(0));

        verify(mockView).showAddContestantButton(1);
    }

    @Test
    public void onContactClickShouldShowAndDecreaseAddContestantButtonWhenOneOfMultipleSelectedContactsIsClicked() {
        List<Contact> list = getMockContactList();
        list.get(0).setSelected(true);
        list.get(1).setSelected(true);
        presenter.onContactListUpdated(list);

        presenter.onContactClick(list.get(0));

        verify(mockView).showAddContestantButton(1);
    }

    @Test
    public void onContactClickShouldHideAddContestantButtonWhenTheOnlySelectedContactIsClicked() {
        List<Contact> list = getMockContactList();
        list.get(0).setSelected(true);
        presenter.onContactListUpdated(list);

        presenter.onContactClick(list.get(0));

        verify(mockView).hideAddContestantButton();
    }

    private List<Contact> getMockContactList() {
        final String EMPTY = "";
        final String TEST_PHONE = "555-555-5555";
        final String TEST_EMAIL = "email@test.com";
        List<Contact> contactList = new ArrayList<>();

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

        Contact noPhoneOrEmail = new Contact();
        noPhoneOrEmail.setPhone(EMPTY);
        noPhoneOrEmail.setEmail(EMPTY);
        contactList.add(noPhoneOrEmail);

        return contactList;
    }
}
