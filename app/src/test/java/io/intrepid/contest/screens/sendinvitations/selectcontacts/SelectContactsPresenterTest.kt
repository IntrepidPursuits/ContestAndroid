package io.intrepid.contest.screens.sendinvitations.selectcontacts

import io.intrepid.contest.R
import io.intrepid.contest.models.Contact
import io.intrepid.contest.models.ParticipationType
import io.intrepid.contest.screens.sendinvitations.selectcontacts.SelectContactsContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import java.util.*

class SelectContactsPresenterTest : BasePresenterTest<SelectContactsPresenter>() {
    @Mock
    private lateinit var mockView: View

    private fun setupContactSelectionScreen(participationType: ParticipationType = ParticipationType.CONTESTANT) {
        setup(participationType, true, ArrayList<Contact>())
    }

    private fun setupPreviewContactsScreen(participationType: ParticipationType) {
        setup(participationType, false, getMockContactList(true))
    }

    private fun setup(participationType: ParticipationType, contactSelectionEnabled: Boolean, contactList: List<Contact>) {
        presenter = SelectContactsPresenter(mockView,
                testConfiguration,
                participationType,
                contactSelectionEnabled,
                contactList)
    }

    @Test
    fun onViewCreatedShouldSetupAdapterWithContactSelectionWhenShowingContactSelectionScreen() {
        setupContactSelectionScreen()
        presenter.onViewCreated()
        verify<View>(mockView).setupAdapter(true)
    }

    @Test
    fun onViewCreatedShouldSetupAdapterWithoutContactSelectionWhenShowingPreviewContactsScreen() {
        setupPreviewContactsScreen(ParticipationType.CONTESTANT)
        presenter.onViewCreated()
        verify<View>(mockView).setupAdapter(false)
    }

    @Test
    fun onViewCreatedShouldDisplayPhoneContactListWhenShowingContactSelectionScreen() {
        setupContactSelectionScreen()
        presenter.onViewCreated()
        verify<View>(mockView).displayPhoneContactList()
    }

    @Test
    fun onViewCreatedShouldShowProgressBarWhenShowingContactSelectionScreen() {
        setupContactSelectionScreen()
        presenter.onViewCreated()
        verify<View>(mockView).showProgressBar(true)
    }

    @Test
    fun onViewCreatedShouldUpdateAdapterContactListWhenShowingPreviewContactsScreen() {
        setupPreviewContactsScreen(ParticipationType.CONTESTANT)
        presenter.onViewCreated()
        verify<View>(mockView).updateAdapterContactList(any<List<Contact>>())
    }

    @Test
    fun onViewCreatedShouldHideProgressBarWhenShowingPreviewContactsScreen() {
        setupPreviewContactsScreen(ParticipationType.CONTESTANT)
        presenter.onViewCreated()
        verify<View>(mockView).showProgressBar(false)
    }

    @Test
    fun onCreateOptionsMenuShouldCreateMenuSearchItemWhenShowingContactSelectionScreen() {
        setupContactSelectionScreen()
        presenter.onCreateOptionsMenu()
        verify<View>(mockView).createMenuSearchItem()
    }

    @Test
    fun onCreateOptionsMenuShouldNotCreateMenuSearchItemWhenShowingPreviewContactsScreen() {
        setupPreviewContactsScreen(ParticipationType.CONTESTANT)
        presenter.onCreateOptionsMenu()
        verify<View>(mockView, never()).createMenuSearchItem()
    }

    @Test
    fun onContactListUpdatedShouldHideProgressBar() {
        setupContactSelectionScreen()
        presenter.onContactListUpdated(getMockContactList(false))
        verify<View>(mockView).showProgressBar(false)
    }

    @Test
    fun onContactListUpdatedShouldUpdateContactListFilteringOutContactsWithoutPhoneOrEmail() {
        setupContactSelectionScreen()
        val initialContactList = getMockContactList(false)

        presenter.onContactListUpdated(initialContactList)

        verify<View>(mockView).updateAdapterContactList(
                argThat<List<Contact>> { argument -> argument.size == initialContactList.size - 1 })
    }

    @Test
    fun onContactListUpdatedShouldUpdateAdapterContactListKeepingDataFromContactWhenInExistedInPreviousList() {
        // Initial contact list
        setupContactSelectionScreen()
        val initialList = getMockContactList(false)
        initialList[0].isSelected = true
        presenter.onContactListUpdated(initialList)
        reset<View>(mockView)

        // Update list
        val listAfterSearch = getMockContactList(false).subList(0, 1)
        listAfterSearch[0].isSelected = false

        presenter.onContactListUpdated(listAfterSearch)

        verify<View>(mockView).updateAdapterContactList(
                argThat<List<Contact>> { adapterList -> adapterList[0] === initialList[0] })
    }

    @Test
    fun onQueryTextSubmitShouldAlwaysReturnTrue() {
        setupContactSelectionScreen()
        assertTrue(presenter.onQueryTextSubmit(""))
    }

    @Test
    fun onQueryTextChangeShouldUpdateContactSearchFilterWhenTextIsEmpty() {
        setupContactSelectionScreen()
        presenter.onQueryTextChange("")
        verify<View>(mockView).updateContactSearchFilter(anyString())
    }

    @Test
    fun onQueryTextChangeShouldNotUpdateContactSearchFilterWhenTextHasOneCharacter() {
        setupContactSelectionScreen()
        presenter.onQueryTextChange("1")
        verify<View>(mockView, never()).updateContactSearchFilter(anyString())
    }

    @Test
    fun onQueryTextChangeShouldUpdateContactSearchFilterWhenTextHasTwoCharactersOrMore() {
        setupContactSelectionScreen()
        presenter.onQueryTextChange("12")
        verify<View>(mockView).updateContactSearchFilter(anyString())
    }

    @Test
    fun onQueryTextChangeShouldHideProgressBarWhenTextIsEmpty() {
        setupContactSelectionScreen()
        presenter.onQueryTextChange("")
        verify<View>(mockView).showProgressBar(false)
    }

    @Test
    fun onQueryTextChangeShouldShowProgressBarWhenTextIsNotEmpty() {
        setupContactSelectionScreen()
        presenter.onQueryTextChange("1")
        verify<View>(mockView).showProgressBar(true)
    }

    @Test
    fun onContactClickShouldDoNothingWhenShowingPreviewContactsScreen() {
        setupPreviewContactsScreen(ParticipationType.CONTESTANT)
        presenter.onContactListUpdated(getMockContactList(false))

        presenter.onContactClick(Contact())

        verify<View>(mockView, never()).onContactSelected()
        verify<View>(mockView, never()).showAddContactsButton(anyInt(), anyInt())
    }

    @Test
    fun onContactClickShouldNotifyAdapterDataSetChanged() {
        setupContactSelectionScreen()
        val contactList = getMockContactList(false)
        presenter.onContactListUpdated(contactList)

        presenter.onContactClick(contactList[0])

        verify<View>(mockView).onContactSelected()
    }

    @Test
    fun onContactClickShouldShowAndIncreaseAddContestantsButtonWhenUnselectedContestantIsClicked() {
        setupContactSelectionScreen()
        val list = getMockContactList(false)
        presenter.onContactListUpdated(list)

        presenter.onContactClick(list[0])

        verify<View>(mockView).showAddContactsButton(1, R.plurals.numberOfContestants)
    }

    @Test
    fun onContactClickShouldShowAndIncreaseAddJudgesButtonWhenUnselectedJudgeIsClicked() {
        setupContactSelectionScreen(ParticipationType.JUDGE)
        val list = getMockContactList(false)
        presenter.onContactListUpdated(list)

        presenter.onContactClick(list[0])

        verify<View>(mockView).showAddContactsButton(1, R.plurals.numberOfJudges)
    }

    @Test
    fun onContactClickShouldShowAndDecreaseAddContestantsButtonWhenOneOfMultipleSelectedContestantsClicked() {
        setupContactSelectionScreen()
        val list = getMockContactList(false)
        list[0].isSelected = true
        list[1].isSelected = true
        presenter.onContactListUpdated(list)

        presenter.onContactClick(list[0])

        verify<View>(mockView).showAddContactsButton(1, R.plurals.numberOfContestants)
    }

    @Test
    fun onContactClickShouldShowAndDecreaseAddJudgesButtonWhenOneOfMultipleSelectedJudgesClicked() {
        setupContactSelectionScreen(ParticipationType.JUDGE)
        val list = getMockContactList(false)
        list[0].isSelected = true
        list[1].isSelected = true
        presenter.onContactListUpdated(list)

        presenter.onContactClick(list[0])

        verify<View>(mockView).showAddContactsButton(1, R.plurals.numberOfJudges)
    }

    @Test
    fun onContactClickShouldHideAddContactsButtonWhenTheOnlySelectedContactIsClicked() {
        setupContactSelectionScreen()
        val list = getMockContactList(false)
        list[0].isSelected = true
        presenter.onContactListUpdated(list)

        presenter.onContactClick(list[0])

        verify<View>(mockView).hideAddContactsButton()
    }

    @Test
    fun onAddParticipantsButtonClickedShouldShowSendInvitationsScreen() {
        setupContactSelectionScreen()
        presenter.onAddContactsButtonClicked()
        verify<View>(mockView).showSendInvitationsScreen(anyList<Contact>())
    }

    private fun getMockContactList(selectOneValidContact: Boolean): List<Contact> {
        val EMPTY = ""
        val TEST_PHONE = "555-555-5555"
        val TEST_EMAIL = "email@test.com"

        val onlyPhone = Contact().apply {
            id = 1
            phone = TEST_PHONE
            email = EMPTY
            isSelected = selectOneValidContact
        }

        val onlyEmail = Contact().apply {
            id = 2
            phone = EMPTY
            email = TEST_EMAIL
        }

        val phoneAndEmail = Contact().apply {
            id = 3
            phone = TEST_PHONE
            email = TEST_EMAIL
        }

        val noPhoneOrEmail = Contact().apply {
            id = 4
            phone = EMPTY
            email = EMPTY
        }

        return listOf(onlyPhone, onlyEmail, phoneAndEmail, noPhoneOrEmail)
    }
}
