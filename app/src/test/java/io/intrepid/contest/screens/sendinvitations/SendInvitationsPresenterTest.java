package io.intrepid.contest.screens.sendinvitations;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.intrepid.contest.R;
import io.intrepid.contest.models.Contact;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.BatchInviteResponse;
import io.intrepid.contest.rest.InvitationResponse;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendInvitationsPresenterTest extends BasePresenterTest<SendInvitationsPresenter> {
    @Mock
    SendInvitationsContract.View mockView;

    @Before
    public void setup() {
        presenter = new SendInvitationsPresenter(mockView, testConfiguration);
    }

    @Test
    public void onViewCreatedShouldShowInvitationIntroFragmentWhenAppHasPermissions() {
        when(mockView.checkContactsPermissions()).thenReturn(true);
        presenter.onViewCreated();
        verify(mockView).showInvitationIntroFragment();
    }

    @Test
    public void onViewCreatedShouldShowInvitationIntroFragmentWhenAppDoesNotHavePermissions() {
        when(mockView.checkContactsPermissions()).thenReturn(false);
        presenter.onViewCreated();
        verify(mockView).showInvitationIntroFragment();
    }

    @Test
    public void onViewBoundShouldRequestContactsPermissionsWhenDoesNotHavePermissions() {
        when(mockView.checkContactsPermissions()).thenReturn(false);
        presenter.onViewBound();
        verify(mockView).requestContactsPermissions();
    }

    @Test
    public void onViewBoundShouldShowSelectContactsFragmentWhenPreviouslyShowedSelectContactsContent() {
        showSelectContactsContent();
        reset(mockView);

        presenter.onViewBound();

        verify(mockView).showSelectContactsFragment();
    }

    @Test
    public void onViewBoundShouldShowSelectContactsFragmentWhenAppHasPermissionsAndPreviouslyShowedPreviewContacts() {
        showPreviewContactsContent(getMockContactList(true));
        reset(mockView);
        when(mockView.checkContactsPermissions()).thenReturn(true);

        presenter.onViewBound();

        verify(mockView).showSelectContactsFragment();
    }

    @Test
    public void onViewBoundShouldShowInvitationIntroFragmentWhenAppDoesNotHavePermissions() {
        showPreviewContactsContent(getMockContactList(true));
        reset(mockView);
        when(mockView.checkContactsPermissions()).thenReturn(false);

        presenter.onViewBound();

        verify(mockView).showInvitationIntroFragment();
    }
    @Test
    public void onViewBoundShouldShowInvitationIntroFragmentWhenPreviouslyShowedInvitationIntroContent() {
        showPreviewContactsContent(getMockContactList(false));
        reset(mockView);
        when(mockView.checkContactsPermissions()).thenReturn(true);

        presenter.onViewBound();

        verify(mockView).showInvitationIntroFragment();
    }

    @Test
    public void isContactSelectionEnabledShouldReturnTrueWhenShowingSelectContactsContent() {
        showSelectContactsContent();
        assertTrue(presenter.isContactSelectionEnabled());
    }

    @Test
    public void isContactSelectionEnabledShouldReturnFalseWhenShowingPreviewContactsContent() {
        showPreviewContactsContent(getMockContactList(false));
        assertFalse(presenter.isContactSelectionEnabled());
    }

    @Test
    public void getContactListShouldReturnFullContactListWhenShowingSelectContactsContent() {
        List<Contact> fullContactList = getMockContactList(true);
        presenter.onAddContestantsButtonClicked(fullContactList);
        showSelectContactsContent();

        List<Contact> contactList = presenter.getContactList();

        assertEquals(fullContactList.size(), contactList.size());
    }

    @Test
    public void getContactListShouldReturnFullContactListWhenShowingPreviewContactsContent() {
        showPreviewContactsContent(getMockContactList(true));
        List<Contact> contactList = presenter.getContactList();
        assertEquals(1, contactList.size());
    }

    @Test
    public void getParticipationTypeShouldReturnContestantWhenViewIsShowingContestants() {
        setParticipationType(ParticipationType.CONTESTANT);
        assertEquals(ParticipationType.CONTESTANT, presenter.getParticipationType());
    }

    @Test
    public void getParticipationTypeShouldReturnJudgeWhenViewIsShowingJudges() {
        setParticipationType(ParticipationType.JUDGE);
        assertEquals(ParticipationType.JUDGE, presenter.getParticipationType());
    }

    @Test
    public void onViewBoundShouldShowInvitationIntroFragment() {
        presenter.onViewBound();
        verify(mockView).showInvitationIntroFragment();
    }

    @Test
    public void onViewBoundShouldRequestContactsPermissionsWhenAppDoesNotHavePermissions() {
        when(mockView.checkContactsPermissions()).thenReturn(false);
        presenter.onViewBound();
        verify(mockView).requestContactsPermissions();
    }

    @Test
    public void onViewBoundShouldNotRequestContactsPermissionsWhenAppHasPermissions() {
        when(mockView.checkContactsPermissions()).thenReturn(true);
        presenter.onViewBound();
        verify(mockView, never()).requestContactsPermissions();
    }

    @Test
    public void onSelectContactsButtonClickedShouldShowSelectContactsScreen() {
        presenter.onSelectContactsButtonClicked();
        verify(mockView).showSelectContactsFragment();
    }

    @Test
    public void onSelectContactsButtonClickedShouldNotShowSelectContactsButton() {
        presenter.onSelectContactsButtonClicked();
        verify(mockView).showSelectContactsButton(false);
    }

    @Test
    public void onContactsPermissionsResultShouldShowSelectContactsButtonWhenPermissionHasBeenGranted() {
        when(mockView.checkContactsPermissions()).thenReturn(true);
        presenter.onContactsPermissionsResult();
        verify(mockView).showSelectContactsButton(true);
    }

    @Test
    public void onContactsPermissionsResultShouldShowInvitationIntroWhenPermissionHasBeenDenied() {
        when(mockView.checkContactsPermissions()).thenReturn(false);
        presenter.onContactsPermissionsResult();
        verify(mockView).showInvitationIntroFragment();
    }

    @Test
    public void onContactsPermissionsResultShouldShowInvitationIntroWhenNoContactsAreSelectedForPreview() {
        when(mockView.checkContactsPermissions()).thenReturn(true);
        presenter.onContactsPermissionsResult();
        verify(mockView).showInvitationIntroFragment();
    }

    @Test
    public void onBackButtonClickedShouldShowPreviewInvitationWhenShowingSelectContactsContent() {
        showSelectContactsContent();
        presenter.onBackButtonClicked();
        verify(mockView).showInvitationIntroFragment();
    }

    @Test
    public void onContactsPermissionsResultShouldHideSelectContactsButtonWhenPermissionHasBeenDenied() {
        when(mockView.checkContactsPermissions()).thenReturn(false);
        presenter.onContactsPermissionsResult();
        verify(mockView).showSelectContactsButton(false);
    }

    @Test
    public void onContactsPermissionsResultShouldShowSelectContactsWhenAppHasPermissionsAndContactsHaveBeenSelected() {
        presenter.onAddContestantsButtonClicked(getMockContactList(true));
        when(mockView.checkContactsPermissions()).thenReturn(true);

        presenter.onContactsPermissionsResult();

        verify(mockView).showSelectContactsFragment();
    }

    @Test
    public void onCreateOptionsMenuShouldHideSendInvitationsMenuItemWhenShowingPreviewWithNoContactsSelected() {
        setParticipationType(ParticipationType.CONTESTANT);
        showPreviewContactsContent(getMockContactList(false));

        presenter.onCreateOptionsMenu();

        verify(mockView).showSendInvitationsMenuItem(false);
    }

    @Test
    public void onCreateOptionsMenuShouldShowSendInvitationsSkipMenuItemWhenShowingPreviewWithNoContactsSelected() {
        setParticipationType(ParticipationType.CONTESTANT);
        showPreviewContactsContent(getMockContactList(false));

        presenter.onCreateOptionsMenu();

        verify(mockView).showSendInvitationsSkipMenuItem(true);
    }

    @Test
    public void onCreateOptionsMenuShouldShowSendInvitationsMenuItemWhenShowingPreviewWithContactsSelected() {
        setParticipationType(ParticipationType.CONTESTANT);
        showPreviewContactsContent(getMockContactList(true));

        presenter.onCreateOptionsMenu();

        verify(mockView).showSendInvitationsMenuItem(true);
    }

    @Test
    public void onCreateOptionsMenuShouldHideSendInvitationsSkipMenuItemWhenShowingPreviewWithContactsSelected() {
        setParticipationType(ParticipationType.CONTESTANT);
        showPreviewContactsContent(getMockContactList(true));

        presenter.onCreateOptionsMenu();

        verify(mockView).showSendInvitationsSkipMenuItem(false);
    }

    @Test
    public void onCreateOptionsMenuShouldSetContestantActionBarTitleWhenShowingPreviewContentInvitingContestants() {
        setParticipationType(ParticipationType.CONTESTANT);
        showPreviewContactsContent(getMockContactList(true));

        presenter.onCreateOptionsMenu();

        verify(mockView).setActionBarTitle(R.string.invite_contestants_bar_title);
    }

    @Test
    public void onCreateOptionsMenuShouldSetJudgeActionBarTitleWhenShowingPreviewContentInvitingJudges() {
        setParticipationType(ParticipationType.JUDGE);
        showPreviewContactsContent(getMockContactList(true));

        presenter.onCreateOptionsMenu();

        verify(mockView).setActionBarTitle(R.string.invite_judges_bar_title);
    }

    @Test
    public void onCreateOptionsMenuShouldHideMenuItemsWhenShowingSelectContactsContent() {
        showSelectContactsContent();

        presenter.onCreateOptionsMenu();

        verify(mockView).showSendInvitationsMenuItem(false);
        verify(mockView).showSendInvitationsSkipMenuItem(false);
    }

    @Test
    public void onOptionsItemSelectedShouldShowInvitationIntroWhenItemIsSendInvitationsSkipInvitingContestants() {
        setParticipationType(ParticipationType.CONTESTANT);
        presenter.onOptionsItemSelected(R.id.send_invitations_skip_menu_action);
        verify(mockView, atLeastOnce()).showInvitationIntroFragment();
    }

    @Test
    public void onOptionsItemSelectedShouldSetParticipationJudgeWhenItemIsSendInvitationsSkipInvitingContestants() {
        setParticipationType(ParticipationType.CONTESTANT);
        presenter.onOptionsItemSelected(R.id.send_invitations_skip_menu_action);
        assertEquals(ParticipationType.JUDGE, presenter.getParticipationType());
    }

    @Test
    public void onOptionsItemSelectedShouldShowContestStatusScreenWhenItemIsSendInvitationsSkipInvitingJudges() {
        setParticipationType(ParticipationType.JUDGE);
        presenter.onOptionsItemSelected(R.id.send_invitations_skip_menu_action);
        verify(mockView).showContestStatusScreen();
    }

    @Test
    public void onOptionsItemSelectedShouldShowErrorMessageWhenWhenItemIsSendInvitationsAndNoContactsWereSelected() {
        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action);
        verify(mockView).showMessage(R.string.no_contacts_selected);
    }

    @Test
    public void onOptionsItemSelectedShouldSetParticipationTypeJudgeWhenSuccessfullySentContestantInvitations() {
        setParticipationType(ParticipationType.CONTESTANT);
        setupSuccessfulInvitationApiCall();

        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action);
        testConfiguration.triggerRxSchedulers();

        assertEquals(ParticipationType.JUDGE, presenter.getParticipationType());
    }

    @Test
    public void onOptionsItemSelectedShouldShowInvitationIntroWhenSuccessfullySentContestantInvitations() {
        setParticipationType(ParticipationType.CONTESTANT);
        setupSuccessfulInvitationApiCall();

        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action);
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showInvitationIntroFragment();
    }

    @Test
    public void onOptionsItemSelectedShouldShowContestStatusScreenWhenSuccessfullySentJudgeInvitations() {
        setParticipationType(ParticipationType.JUDGE);
        showPreviewContactsContent(getMockContactList(true));

        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action);
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showContestStatusScreen();
    }

    @Test
    public void onOptionsItemSelectedShouldShowApiErrorMessageWhenItemIsSendInvitationsAndApiCallThrowsError()
            throws HttpException {
        showPreviewContactsContent(getMockContactList(true));
        reset(mockView);
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        when(mockRestApi.batchInvite(any(), any())).thenReturn(error(new Throwable()));

        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action);
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }

    private void showSelectContactsContent() {
        presenter.onSelectContactsButtonClicked();
    }

    private void showPreviewContactsContent(List<Contact> contactList) {
        presenter.onAddContestantsButtonClicked(contactList);
        when(mockView.checkContactsPermissions()).thenReturn(true);
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

    private void setParticipationType(ParticipationType participationType) {
        presenter.onViewCreated();
        if (participationType.equals(ParticipationType.JUDGE)) {
            setupSuccessfulInvitationApiCall();
            presenter.onOptionsItemSelected(R.id.send_invitations_menu_action);
            testConfiguration.triggerRxSchedulers();
        }
        reset(mockView);
    }

    private void setupSuccessfulInvitationApiCall() {
        showPreviewContactsContent(getMockContactList(true));
        reset(mockView);
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        BatchInviteResponse batchInviteResponse = new BatchInviteResponse();
        batchInviteResponse.invitationResponses = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            InvitationResponse response = new InvitationResponse();
            response.code = "code" + i;
            batchInviteResponse.invitationResponses.add(response);
        }
        when(mockRestApi.batchInvite(any(), any())).thenReturn(Observable.just(batchInviteResponse));
    }
}
