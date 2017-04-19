package io.intrepid.contest.screens.contestjudging.scoreentries

import io.intrepid.contest.models.Category
import io.intrepid.contest.models.Contest
import io.intrepid.contest.models.Entry
import io.intrepid.contest.rest.ContestWrapper
import io.intrepid.contest.screens.contestjudging.scoreentries.ScoresEntriesContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable
import io.reactivex.Observable.error
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.*
import java.util.*

class ScoreEntriesPresenterTest : BasePresenterTest<ScoreEntriesPresenter>() {
    private val TEST_ENTRY_IMAGE = "https://www.chowstatic.com/assets/2014/09/30669_spicy_slow_cooker_beef_chili_3000x2000.jpg"
    private val entriesList = makeListOfEntries()
    private val categoriesList = makeListOfCategories()

    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockEntry: Entry

    @Before
    fun setup() {
        presenter = ScoreEntriesPresenter(mockView, testConfiguration)
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
    }

    private fun makeListOfEntries(): List<Entry> {
        val entries = ArrayList<Entry>()
        for (i in 0..2) {
            val entry = Entry()
            entry.title = "Test Entry " + i
            entry.photoUrl = TEST_ENTRY_IMAGE
            entries.add(entry)
        }
        return entries
    }

    private fun makeListOfCategories(): List<Category> {
        val categories = ArrayList<Category>()
        for (i in 0..2) {
            categories.add(Category("Test category " + i, "Description"))
        }
        return categories
    }

    private fun setupSuccessfulContestDetailsCall() {
        val contest = Contest()
        contest.title = "Contest title"
        contest.entries = entriesList
        contest.categories = categoriesList

        val response = ContestWrapper(contest)
        `when`(mockRestApi.getContestDetails(any<String>())).thenReturn(Observable.just(response))

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()
    }

    private fun setupFailureContestDetailsCall() {
        `when`(mockRestApi.getContestDetails(any<String>())).thenReturn(error<ContestWrapper>(Throwable()))

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()
    }

    @Test
    fun onViewCreatedShouldTriggerViewToDisplayListOfEntries() {
        setupSuccessfulContestDetailsCall()
        verify<View>(mockView).showEntriesList()
    }

    @Test
    fun fetchEntriesFailureShouldTriggerViewToShowError() {
        setupFailureContestDetailsCall()
        verify<View>(mockView).showMessage(anyInt())
    }

    @Test
    fun onBackPressedShouldTriggerViewToShowEntriesListWhenViewingAnEntry() {
        setupSuccessfulContestDetailsCall()
        presenter.onEntrySelected(entriesList[0])
        reset<View>(mockView)

        presenter.onBackPressed()

        verify<View>(mockView).showEntriesList()
    }

    @Test
    fun onBackPressedShouldTriggerViewToCancelScoringEntriesWhenNotViewingAnEntry() {
        presenter.onBackPressed()
        verify<View>(mockView).cancelScoringEntries()
    }

    @Test
    fun onBackPressedShouldTriggerViewToShowEntriesListAfterScoringHasStarted() {
        setupSuccessfulContestDetailsCall()
        presenter.onNextClicked()

        presenter.onBackPressed()

        verify<View>(mockView).showEntriesList()
    }

    @Test
    fun getCategoriesShouldReturnCategoriesWhenSuccessfullyRetrievedFromApiCall() {
        setupSuccessfulContestDetailsCall()
        assertEquals(categoriesList, presenter.categories)
    }

    @Test
    fun getEntriesShouldReturnEntriesWhenSuccessfullyRetrievedFromApiCall() {
        setupSuccessfulContestDetailsCall()
        assertEquals(entriesList, presenter.entries)
    }

    @Test
    fun getEntryBallotsListShouldReturnBallotsCorrespondingToEntriesSuccessfullyRetrievedFromApiCall() {
        setupSuccessfulContestDetailsCall()
        assertEquals(entriesList.size, presenter.entryBallotsList.size)
    }

    @Test
    fun getCurrentEntryShouldReturnCorrectEntryWhenViewingIt() {
        setupSuccessfulContestDetailsCall()
        presenter.onEntrySelected(entriesList[0])

        assertEquals(entriesList[0], presenter.currentEntry)
    }

    @Test
    fun getCurrentEntryShouldReturnNullWhenNotViewingAnEntry() {
        setupSuccessfulContestDetailsCall()
        assertNull(presenter.currentEntry)
    }

    @Test
    fun getCurrentEntryBallotShouldReturnCorrectBallotWhenViewingEntry() {
        setupSuccessfulContestDetailsCall()
        presenter.onEntrySelected(entriesList[0])
        val entryBallotList = presenter.entryBallotsList

        assertEquals(entryBallotList[0], presenter.currentEntryBallot)
    }

    @Test
    fun getCurrentEntryBallotShouldReturnNullWhenNotViewingAnEntry() {
        setupSuccessfulContestDetailsCall()
        assertEquals(null, presenter.currentEntryBallot)
    }

    @Test
    fun onNextClickedShouldTriggerViewToShowNextEntryWhenNotViewingLastEntry() {
        setupSuccessfulContestDetailsCall()
        presenter.onEntrySelected(entriesList[0])
        reset<View>(mockView)

        presenter.onNextClicked()

        verify<View>(mockView).showEntryDetail(anyInt())
    }

    @Test
    fun onNextClickedShouldTriggerViewToShowEntriesListWhenViewingLastEntry() {
        setupSuccessfulContestDetailsCall()
        presenter.onEntrySelected(entriesList[entriesList.size - 1])
        reset<View>(mockView)

        presenter.onNextClicked()

        verify<View>(mockView).showEntriesList()
    }
}
