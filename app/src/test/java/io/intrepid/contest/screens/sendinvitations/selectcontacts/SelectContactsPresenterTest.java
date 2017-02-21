package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.R;
import io.intrepid.contest.models.Contact;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.testutils.BasePresenterTest;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

public class SelectContactsPresenterTest extends BasePresenterTest<SelectContactsPresenter> {
    @Mock
    SelectContactsContract.View mockView;

    private void setupContactSelectionScreen(ParticipationType participationType) {
        setup(participationType, true, new ArrayList<>());
    }

    private void setupContactSelectionScreen() {
        setupContactSelectionScreen(ParticipationType.CONTESTANT);
    }

    private void setupPreviewContactsScreen(ParticipationType participationType) {
        setup(participationType, false, getMockContactList(true));
    }

    private void setup(ParticipationType participationType, boolean contactSelectionEnabled, List<Contact> contactList) {
        presenter = new SelectContactsPresenter(mockView,
                                                testConfiguration,
                                                participationType,
                                                contactSelectionEnabled,
                                                contactList);
    }

    @Test
    public void onViewCreatedShouldSetupAdapterWithContactSelectionWhenShowingContactSelectionScreen() {
        setupContactSelectionScreen();
        presenter.onViewCreated();
        verify(mockView).setupAdapter(true);
    }

    @Test
    public void onViewCreatedShouldSetupAdapterWithoutContactSelectionWhenShowingPreviewContactsScreen() {
        setupPreviewContactsScreen(ParticipationType.CONTESTANT);
        presenter.onViewCreated();
        verify(mockView).setupAdapter(false);
    }

    @Test
    public void onViewCreatedShouldDisplayPhoneContactListWhenShowingContactSelectionScreen() {
        setupContactSelectionScreen();
        presenter.onViewCreated();
        verify(mockView).displayPhoneContactList();
    }

    @Test
    public void onViewCreatedShouldShowProgressBarWhenShowingContactSelectionScreen() {
        setupContactSelectionScreen();
        presenter.onViewCreated();
        verify(mockView).showProgressBar(true);
    }

    @Test
    public void onViewCreatedShouldUpdateAdapterContactListWhenShowingPreviewContactsScreen() {
        setupPreviewContactsScreen(ParticipationType.CONTESTANT);
        presenter.onViewCreated();
        verify(mockView).updateAdapterContactList(any());
    }

    @Test
    public void onViewCreatedShouldHideProgressBarWhenShowingPreviewContactsScreen() {
        setupPreviewContactsScreen(ParticipationType.CONTESTANT);
        presenter.onViewCreated();
        verify(mockView).showProgressBar(false);
    }

    @Test
    public void onCreateOptionsMenuShouldCreateMenuSearchItemWhenShowingContactSelectionScreen() {
        setupContactSelectionScreen();
        presenter.onCreateOptionsMenu();
        verify(mockView).createMenuSearchItem();
    }

    @Test
    public void onCreateOptionsMenuShouldNotCreateMenuSearchItemWhenShowingPreviewContactsScreen() {
        setupPreviewContactsScreen(ParticipationType.CONTESTANT);
        presenter.onCreateOptionsMenu();
        verify(mockView, never()).createMenuSearchItem();
    }

    @Test
    public void onContactListUpdatedShouldHideProgressBar() {
        setupContactSelectionScreen();
        presenter.onContactListUpdated(getMockContactList(false));
        verify(mockView).showProgressBar(false);
    }

    @Test
    public void onContactListUpdatedShouldUpdateContactListFilteringOutContactsWithoutPhoneOrEmail() {
        setupContactSelectionScreen();
        List<Contact> initialContactList = getMockContactList(false);

        presenter.onContactListUpdated(initialContactList);

        verify(mockView).updateAdapterContactList(argThat(argument -> argument.size() ==
                initialContactList.size() - 1));
    }

    @Test
    public void onContactListUpdatedShouldUpdateAdapterContactListKeepingDataFromContactWhenInExistedInPreviousList() {
        // Initial contact list
        setupContactSelectionScreen();
        List<Contact> initialList = getMockContactList(false);
        initialList.get(0).setSelected(true);
        presenter.onContactListUpdated(initialList);
        reset(mockView);
        // Update list
        List<Contact> listAfterSearch = getMockContactList(false).subList(0, 1);
        listAfterSearch.get(0).setSelected(false);

        presenter.onContactListUpdated(listAfterSearch);

        verify(mockView).updateAdapterContactList(
                argThat(adapterList -> (adapterList.get(0) == initialList.get(0))));
    }

    @Test
    public void onQueryTextSubmitShouldAlwaysReturnTrue() {
        setupContactSelectionScreen();
        assertTrue(presenter.onQueryTextSubmit(""));
    }

    @Test
    public void onQueryTextChangeShouldUpdateContactSearchFilterWhenTextIsEmpty() {
        setupContactSelectionScreen();
        presenter.onQueryTextChange("");
        verify(mockView).updateContactSearchFilter(anyString());
    }

    @Test
    public void onQueryTextChangeShouldNotUpdateContactSearchFilterWhenTextHasOneCharacter() {
        setupContactSelectionScreen();
        presenter.onQueryTextChange("1");
        verify(mockView, never()).updateContactSearchFilter(anyString());
    }

    @Test
    public void onQueryTextChangeShouldUpdateContactSearchFilterWhenTextHasTwoCharactersOrMore() {
        setupContactSelectionScreen();
        presenter.onQueryTextChange("12");
        verify(mockView).updateContactSearchFilter(anyString());
    }

    @Test
    public void onQueryTextChangeShouldHideProgressBarWhenTextIsEmpty() {
        setupContactSelectionScreen();
        presenter.onQueryTextChange("");
        verify(mockView).showProgressBar(false);
    }

    @Test
    public void onQueryTextChangeShouldShowProgressBarWhenTextIsNotEmpty() {
        setupContactSelectionScreen();
        presenter.onQueryTextChange("1");
        verify(mockView).showProgressBar(true);
    }

    @Test
    public void onContactClickShouldDoNothingWhenShowingPreviewContactsScreen() {
        setupPreviewContactsScreen(ParticipationType.CONTESTANT);
        presenter.onContactListUpdated(getMockContactList(false));

        presenter.onContactClick(mock(Contact.class));

        verify(mockView, never()).onContactSelected();
        verify(mockView, never()).showAddContactsButton(anyInt(), anyInt());
    }

    @Test
    public void onContactClickShouldNotifyAdapterDataSetChanged() {
        setupContactSelectionScreen();
        List<Contact> contactList = getMockContactList(false);
        presenter.onContactListUpdated(contactList);

        presenter.onContactClick(contactList.get(0));

        verify(mockView).onContactSelected();
    }

    @Test
    public void onContactClickShouldShowAndIncreaseAddContestantsButtonWhenUnselectedContestantIsClicked() {
        setupContactSelectionScreen();
        List<Contact> list = getMockContactList(false);
        presenter.onContactListUpdated(list);

        presenter.onContactClick(list.get(0));

        verify(mockView).showAddContactsButton(1, R.plurals.numberOfContestants);
    }

    @Test
    public void onContactClickShouldShowAndIncreaseAddJudgesButtonWhenUnselectedJudgeIsClicked() {
        setupContactSelectionScreen(ParticipationType.JUDGE);
        List<Contact> list = getMockContactList(false);
        presenter.onContactListUpdated(list);

        presenter.onContactClick(list.get(0));

        verify(mockView).showAddContactsButton(1, R.plurals.numberOfJudges);
    }

    @Test
    public void onContactClickShouldShowAndDecreaseAddContestantsButtonWhenOneOfMultipleSelectedContestantsClicked() {
        setupContactSelectionScreen();
        List<Contact> list = getMockContactList(false);
        list.get(0).setSelected(true);
        list.get(1).setSelected(true);
        presenter.onContactListUpdated(list);

        presenter.onContactClick(list.get(0));

        verify(mockView).showAddContactsButton(1, R.plurals.numberOfContestants);
    }

    @Test
    public void onContactClickShouldShowAndDecreaseAddJudgesButtonWhenOneOfMultipleSelectedJudgesClicked() {
        setupContactSelectionScreen(ParticipationType.JUDGE);
        List<Contact> list = getMockContactList(false);
        list.get(0).setSelected(true);
        list.get(1).setSelected(true);
        presenter.onContactListUpdated(list);

        presenter.onContactClick(list.get(0));

        verify(mockView).showAddContactsButton(1, R.plurals.numberOfJudges);
    }

    @Test
    public void onContactClickShouldHideAddContactsButtonWhenTheOnlySelectedContactIsClicked() {
        setupContactSelectionScreen();
        List<Contact> list = getMockContactList(false);
        list.get(0).setSelected(true);
        presenter.onContactListUpdated(list);

        presenter.onContactClick(list.get(0));

        verify(mockView).hideAddContactsButton();
    }

    @Test
    public void onAddParticipantsButtonClickedShouldShowSendInvitationsScreen() {
        setupContactSelectionScreen();
        presenter.onAddContactsButtonClicked();
        verify(mockView).showSendInvitationsScreen(anyList());
    }

    private List<Contact> getMockContactList(boolean selectOneValidContact) {
        final String EMPTY = "";
        final String TEST_PHONE = "555-555-5555";
        final String TEST_EMAIL = "email@test.com";
        List<Contact> contactList = new ArrayList<>();

        Contact onlyPhone = new Contact();
        onlyPhone.setId(1);
        onlyPhone.setPhone(TEST_PHONE);
        onlyPhone.setEmail(EMPTY);
        contactList.add(onlyPhone);
        if (selectOneValidContact) {
            onlyPhone.setSelected(true);
        }

        Contact onlyEmail = new Contact();
        onlyEmail.setId(2);
        onlyEmail.setPhone(EMPTY);
        onlyEmail.setEmail(TEST_EMAIL);
        contactList.add(onlyEmail);

        Contact phoneAndEmail = new Contact();
        phoneAndEmail.setId(3);
        phoneAndEmail.setPhone(TEST_PHONE);
        phoneAndEmail.setEmail(TEST_EMAIL);
        contactList.add(phoneAndEmail);

        Contact noPhoneOrEmail = new Contact();
        noPhoneOrEmail.setId(4);
        noPhoneOrEmail.setPhone(EMPTY);
        noPhoneOrEmail.setEmail(EMPTY);
        contactList.add(noPhoneOrEmail);

        return contactList;
    }
}
