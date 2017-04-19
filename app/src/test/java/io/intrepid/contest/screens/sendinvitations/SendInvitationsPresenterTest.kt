package io.intrepid.contest.screens.sendinvitations

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException

import org.junit.Before
import org.junit.Test
import org.mockito.Mock

import java.util.ArrayList
import java.util.UUID

import io.intrepid.contest.R
import io.intrepid.contest.models.Contact
import io.intrepid.contest.models.ParticipationType
import io.intrepid.contest.rest.BatchInviteRequest
import io.intrepid.contest.rest.BatchInviteResponse
import io.intrepid.contest.rest.InvitationResponse
import io.intrepid.contest.screens.sendinvitations.SendInvitationsContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable

import io.reactivex.Observable.error
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.never
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class SendInvitationsPresenterTest : BasePresenterTest<SendInvitationsPresenter>() {
    @Mock
    private lateinit var mockView: View

    @Before
    fun setup() {
        presenter = SendInvitationsPresenter(mockView, testConfiguration)
    }

    @Test
    fun onViewCreatedShouldShowInvitationIntroFragmentWhenAppHasPermissions() {
        `when`(mockView.checkContactsPermissions()).thenReturn(true)
        presenter.onViewCreated()
        verify<View>(mockView).showInvitationIntroFragment()
    }

    @Test
    fun onViewCreatedShouldShowInvitationIntroFragmentWhenAppDoesNotHavePermissions() {
        `when`(mockView.checkContactsPermissions()).thenReturn(false)
        presenter.onViewCreated()
        verify<View>(mockView).showInvitationIntroFragment()
    }

    @Test
    fun onViewBoundShouldRequestContactsPermissionsWhenDoesNotHavePermissions() {
        `when`(mockView.checkContactsPermissions()).thenReturn(false)
        presenter.onViewBound()
        verify<View>(mockView).requestContactsPermissions()
    }

    @Test
    fun onViewBoundShouldShowSelectContactsFragmentWhenPreviouslyShowedSelectContactsContent() {
        showSelectContactsContent()
        reset<View>(mockView)

        presenter.onViewBound()

        verify<View>(mockView).showSelectContactsFragment()
    }

    @Test
    fun onViewBoundShouldShowSelectContactsFragmentWhenAppHasPermissionsAndPreviouslyShowedPreviewContacts() {
        showPreviewContactsContent(getMockContactList(true))
        reset<View>(mockView)
        `when`(mockView.checkContactsPermissions()).thenReturn(true)

        presenter.onViewBound()

        verify<View>(mockView).showSelectContactsFragment()
    }

    @Test
    fun onViewBoundShouldShowInvitationIntroFragmentWhenAppDoesNotHavePermissions() {
        showPreviewContactsContent(getMockContactList(true))
        reset<View>(mockView)
        `when`(mockView.checkContactsPermissions()).thenReturn(false)

        presenter.onViewBound()

        verify<View>(mockView).showInvitationIntroFragment()
    }

    @Test
    fun onViewBoundShouldShowInvitationIntroFragmentWhenPreviouslyShowedInvitationIntroContent() {
        showPreviewContactsContent(getMockContactList(false))
        reset<View>(mockView)
        `when`(mockView.checkContactsPermissions()).thenReturn(true)

        presenter.onViewBound()

        verify<View>(mockView).showInvitationIntroFragment()
    }

    @Test
    fun isContactSelectionEnabledShouldReturnTrueWhenShowingSelectContactsContent() {
        showSelectContactsContent()
        assertTrue(presenter.isContactSelectionEnabled)
    }

    @Test
    fun isContactSelectionEnabledShouldReturnFalseWhenShowingPreviewContactsContent() {
        showPreviewContactsContent(getMockContactList(false))
        assertFalse(presenter.isContactSelectionEnabled)
    }

    @Test
    fun getContactListShouldReturnFullContactListWhenShowingSelectContactsContent() {
        val fullContactList = getMockContactList(true)
        presenter.onAddContestantsButtonClicked(fullContactList)
        showSelectContactsContent()

        val contactList = presenter.contactList

        assertEquals(fullContactList.size, contactList.size)
    }

    @Test
    fun getContactListShouldReturnFullContactListWhenShowingPreviewContactsContent() {
        showPreviewContactsContent(getMockContactList(true))
        val contactList = presenter.contactList
        assertEquals(1, contactList.size)
    }

    @Test
    fun getParticipationTypeShouldReturnContestantWhenViewIsShowingContestants() {
        setParticipationType(ParticipationType.CONTESTANT)
        assertEquals(ParticipationType.CONTESTANT, presenter.invitationParticipantType)
    }

    @Test
    fun getParticipationTypeShouldReturnJudgeWhenViewIsShowingJudges() {
        setParticipationType(ParticipationType.JUDGE)
        assertEquals(ParticipationType.JUDGE, presenter.invitationParticipantType)
    }

    @Test
    fun onViewBoundShouldShowInvitationIntroFragment() {
        presenter.onViewBound()
        verify<View>(mockView).showInvitationIntroFragment()
    }

    @Test
    fun onViewBoundShouldRequestContactsPermissionsWhenAppDoesNotHavePermissions() {
        `when`(mockView.checkContactsPermissions()).thenReturn(false)
        presenter.onViewBound()
        verify<View>(mockView).requestContactsPermissions()
    }

    @Test
    fun onViewBoundShouldNotRequestContactsPermissionsWhenAppHasPermissions() {
        `when`(mockView.checkContactsPermissions()).thenReturn(true)
        presenter.onViewBound()
        verify<View>(mockView, never()).requestContactsPermissions()
    }

    @Test
    fun onSelectContactsButtonClickedShouldShowSelectContactsScreen() {
        presenter.onSelectContactsButtonClicked()
        verify<View>(mockView).showSelectContactsFragment()
    }

    @Test
    fun onSelectContactsButtonClickedShouldNotShowSelectContactsButton() {
        presenter.onSelectContactsButtonClicked()
        verify<View>(mockView).showSelectContactsButton(false)
    }

    @Test
    fun onContactsPermissionsResultShouldShowSelectContactsButtonWhenPermissionHasBeenGranted() {
        `when`(mockView.checkContactsPermissions()).thenReturn(true)
        presenter.onContactsPermissionsResult()
        verify<View>(mockView).showSelectContactsButton(true)
    }

    @Test
    fun onContactsPermissionsResultShouldShowInvitationIntroWhenPermissionHasBeenDenied() {
        `when`(mockView.checkContactsPermissions()).thenReturn(false)
        presenter.onContactsPermissionsResult()
        verify<View>(mockView).showInvitationIntroFragment()
    }

    @Test
    fun onContactsPermissionsResultShouldShowInvitationIntroWhenNoContactsAreSelectedForPreview() {
        `when`(mockView.checkContactsPermissions()).thenReturn(true)
        presenter.onContactsPermissionsResult()
        verify<View>(mockView).showInvitationIntroFragment()
    }

    @Test
    fun onBackButtonClickedShouldShowPreviewInvitationWhenShowingSelectContactsContent() {
        showSelectContactsContent()
        presenter.onBackButtonClicked()
        verify<View>(mockView).showInvitationIntroFragment()
    }

    @Test
    fun onContactsPermissionsResultShouldHideSelectContactsButtonWhenPermissionHasBeenDenied() {
        `when`(mockView.checkContactsPermissions()).thenReturn(false)
        presenter.onContactsPermissionsResult()
        verify<View>(mockView).showSelectContactsButton(false)
    }

    @Test
    fun onContactsPermissionsResultShouldShowSelectContactsWhenAppHasPermissionsAndContactsHaveBeenSelected() {
        presenter.onAddContestantsButtonClicked(getMockContactList(true))
        `when`(mockView.checkContactsPermissions()).thenReturn(true)

        presenter.onContactsPermissionsResult()

        verify<View>(mockView).showSelectContactsFragment()
    }

    @Test
    fun onCreateOptionsMenuShouldHideSendInvitationsMenuItemWhenShowingPreviewWithNoContactsSelected() {
        setParticipationType(ParticipationType.CONTESTANT)
        showPreviewContactsContent(getMockContactList(false))

        presenter.onCreateOptionsMenu()

        verify<View>(mockView).showSendInvitationsMenuItem(false)
    }

    @Test
    fun onCreateOptionsMenuShouldShowSendInvitationsSkipMenuItemWhenShowingPreviewWithNoContactsSelected() {
        setParticipationType(ParticipationType.CONTESTANT)
        showPreviewContactsContent(getMockContactList(false))

        presenter.onCreateOptionsMenu()

        verify<View>(mockView).showSendInvitationsSkipMenuItem(true)
    }

    @Test
    fun onCreateOptionsMenuShouldShowSendInvitationsMenuItemWhenShowingPreviewWithContactsSelected() {
        setParticipationType(ParticipationType.CONTESTANT)
        showPreviewContactsContent(getMockContactList(true))

        presenter.onCreateOptionsMenu()

        verify<View>(mockView).showSendInvitationsMenuItem(true)
    }

    @Test
    fun onCreateOptionsMenuShouldHideSendInvitationsSkipMenuItemWhenShowingPreviewWithContactsSelected() {
        setParticipationType(ParticipationType.CONTESTANT)
        showPreviewContactsContent(getMockContactList(true))

        presenter.onCreateOptionsMenu()

        verify<View>(mockView).showSendInvitationsSkipMenuItem(false)
    }

    @Test
    fun onCreateOptionsMenuShouldSetContestantActionBarTitleWhenShowingPreviewContentInvitingContestants() {
        setParticipationType(ParticipationType.CONTESTANT)
        showPreviewContactsContent(getMockContactList(true))

        presenter.onCreateOptionsMenu()

        verify<View>(mockView).setActionBarTitle(R.string.invite_contestants_bar_title)
    }

    @Test
    fun onCreateOptionsMenuShouldSetJudgeActionBarTitleWhenShowingPreviewContentInvitingJudges() {
        setParticipationType(ParticipationType.JUDGE)
        showPreviewContactsContent(getMockContactList(true))

        presenter.onCreateOptionsMenu()

        verify<View>(mockView).setActionBarTitle(R.string.invite_judges_bar_title)
    }

    @Test
    fun onCreateOptionsMenuShouldHideMenuItemsWhenShowingSelectContactsContent() {
        showSelectContactsContent()

        presenter.onCreateOptionsMenu()

        verify<View>(mockView).showSendInvitationsMenuItem(false)
        verify<View>(mockView).showSendInvitationsSkipMenuItem(false)
    }

    @Test
    fun onOptionsItemSelectedShouldShowInvitationIntroWhenItemIsSendInvitationsSkipInvitingContestants() {
        setParticipationType(ParticipationType.CONTESTANT)
        presenter.onOptionsItemSelected(R.id.send_invitations_skip_menu_action)
        verify<View>(mockView, atLeastOnce()).showInvitationIntroFragment()
    }

    @Test
    fun onOptionsItemSelectedShouldSetParticipationJudgeWhenItemIsSendInvitationsSkipInvitingContestants() {
        setParticipationType(ParticipationType.CONTESTANT)
        presenter.onOptionsItemSelected(R.id.send_invitations_skip_menu_action)
        assertEquals(ParticipationType.JUDGE, presenter.invitationParticipantType)
    }

    @Test
    fun onOptionsItemSelectedShouldSetJudgeActionBarTitleWhenSkipInvitingContestants() {
        setParticipationType(ParticipationType.CONTESTANT)
        presenter.onOptionsItemSelected(R.id.send_invitations_skip_menu_action)
        verify<View>(mockView).setActionBarTitle(R.string.invite_judges_bar_title)
    }

    @Test
    fun onOptionsItemSelectedShouldShowContestStatusScreenWhenItemIsSendInvitationsSkipInvitingJudges() {
        setParticipationType(ParticipationType.JUDGE)
        presenter.onOptionsItemSelected(R.id.send_invitations_skip_menu_action)
        verify<View>(mockView).showContestStatusScreen()
    }

    @Test
    fun onOptionsItemSelectedShouldShowErrorMessageWhenWhenItemIsSendInvitationsAndNoContactsWereSelected() {
        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action)
        verify<View>(mockView).showMessage(R.string.no_contacts_selected)
    }

    @Test
    fun onOptionsItemSelectedShouldSetParticipationTypeJudgeWhenSuccessfullySentContestantInvitations() {
        setParticipationType(ParticipationType.CONTESTANT)
        setupSuccessfulInvitationApiCall()

        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action)
        testConfiguration.triggerRxSchedulers()

        assertEquals(ParticipationType.JUDGE, presenter.invitationParticipantType)
    }

    @Test
    fun onOptionsItemSelectedShouldShowInvitationIntroWhenSuccessfullySentContestantInvitations() {
        setParticipationType(ParticipationType.CONTESTANT)
        setupSuccessfulInvitationApiCall()

        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showInvitationIntroFragment()
    }

    @Test
    fun onOptionsItemSelectedShouldShowContestStatusScreenWhenSuccessfullySentJudgeInvitations() {
        setParticipationType(ParticipationType.JUDGE)
        showPreviewContactsContent(getMockContactList(true))

        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showContestStatusScreen()
    }

    @Test
    @Throws(HttpException::class)
    fun onOptionsItemSelectedShouldShowApiErrorMessageWhenItemIsSendInvitationsAndApiCallThrowsError() {
        showPreviewContactsContent(getMockContactList(true))
        reset<View>(mockView)
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.batchInvite(any<String>(), any<BatchInviteRequest>())).thenReturn(error<BatchInviteResponse>(Throwable()))

        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(any(Int::class.javaPrimitiveType!!))
    }

    private fun showSelectContactsContent() {
        presenter.onSelectContactsButtonClicked()
    }

    private fun showPreviewContactsContent(contactList: List<Contact>) {
        presenter.onAddContestantsButtonClicked(contactList)
        `when`(mockView.checkContactsPermissions()).thenReturn(true)
    }

    private fun getMockContactList(selectOneValidContact: Boolean): List<Contact> {
        val EMPTY = ""
        val TEST_PHONE = "555-555-5555"
        val TEST_EMAIL = "email@test.com"
        val contactList = ArrayList<Contact>()

        val onlyPhone = Contact()
        onlyPhone.id = 1
        onlyPhone.phone = TEST_PHONE
        onlyPhone.email = EMPTY
        contactList.add(onlyPhone)
        if (selectOneValidContact) {
            onlyPhone.isSelected = true
        }

        val onlyEmail = Contact()
        onlyEmail.id = 2
        onlyEmail.phone = EMPTY
        onlyEmail.email = TEST_EMAIL
        contactList.add(onlyEmail)

        val phoneAndEmail = Contact()
        phoneAndEmail.id = 3
        phoneAndEmail.phone = TEST_PHONE
        phoneAndEmail.email = TEST_EMAIL
        contactList.add(phoneAndEmail)

        val noPhoneOrEmail = Contact()
        noPhoneOrEmail.id = 4
        noPhoneOrEmail.phone = EMPTY
        noPhoneOrEmail.email = EMPTY
        contactList.add(noPhoneOrEmail)

        return contactList
    }

    private fun setParticipationType(participationType: ParticipationType) {
        presenter.onViewCreated()
        if (participationType == ParticipationType.JUDGE) {
            setupSuccessfulInvitationApiCall()
            presenter.onOptionsItemSelected(R.id.send_invitations_menu_action)
            testConfiguration.triggerRxSchedulers()
        }
        reset<View>(mockView)
    }

    private fun setupSuccessfulInvitationApiCall() {
        showPreviewContactsContent(getMockContactList(true))
        reset<View>(mockView)
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        val batchInviteResponse = BatchInviteResponse()
        batchInviteResponse.invitationResponses = ArrayList<InvitationResponse>()
        for (i in 0..2) {
            val response = InvitationResponse()
            response.code = "code" + i
            batchInviteResponse.invitationResponses.add(response)
        }
        `when`(mockRestApi.batchInvite(any<String>(), any<BatchInviteRequest>())).thenReturn(Observable.just(batchInviteResponse))
    }
}
