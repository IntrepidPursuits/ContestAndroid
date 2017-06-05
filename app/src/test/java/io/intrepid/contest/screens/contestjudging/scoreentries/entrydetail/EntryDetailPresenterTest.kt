package io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail

import io.intrepid.contest.models.Category
import io.intrepid.contest.models.Entry
import io.intrepid.contest.models.EntryBallot
import io.intrepid.contest.models.Score
import io.intrepid.contest.screens.contestjudging.scoreentries.entrydetail.EntryDetailContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import java.util.*

class EntryDetailPresenterTest : BasePresenterTest<EntryDetailPresenter>() {
    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockBallot: EntryBallot
    @Mock
    private lateinit var mockCategory: Category

    private lateinit var entry: Entry

    @Before
    fun setup() {
        entry = Entry().apply {
            id = UUID.randomUUID()
        }
        `when`(mockView.entryToRate).thenReturn(entry)
        presenter = EntryDetailPresenter(mockView, testConfiguration)
    }

    @Test
    fun onViewCreatedShouldTriggerViewToShowEntries() {
        presenter.onViewCreated()
        verify<View>(mockView).showEntries(anyList<Entry>())
    }

    @Test
    fun onPageSwipedShouldCauseViewToUpdateToolbarTitle() {
        `when`(mockView.allEntries).thenReturn(listOf(Entry(), Entry()))
        presenter.onViewCreated()

        presenter.onSnapViewSwiped(1)

        verify<View>(mockView).onPageSwipedTo(1)
    }

    @Test
    fun onScoreChangedShouldCauseViewToShowReviewButtonWhenAllBallotsAreScored() {
        makeCompletedBallot()
        presenter.onViewCreated()

        presenter.onScoreChanged(0, 1)

        verify<View>(mockView).setReviewRatingsButtonVisibility(true)
    }

    @Test
    fun onScoreChangedShouldCauseViewToDoNothingWhenAllBallotsAreNotScored() {
        makeIncompleteBallots()
        presenter.onViewCreated()

        presenter.onScoreChanged(0, 1)

        verify<View>(mockView).setReviewRatingsButtonVisibility(false)
    }

    @Test
    fun onEntryScoreReviewClickedShouldCauseViewToReturnToEntriesListPage() {
        presenter.onViewCreated()
        presenter.onEntryScoreReviewClicked()
        verify<View>(mockView).returnToEntriesListPage(true)
    }

    @Test
    fun onPageScrolledShouldCauseViewToSetNextInvisibleWhenEntriesAreNotCompletelyScored() {
        entry.setCategoriesSize(1)
        presenter.onPageScrolled()
        verify<View>(mockView).setNextEnabled(false)
    }

    @Test
    fun onPageScrolledShouldCauseViewToSetNextVisibleWhenEntriesAreCompletelyScored() {
        presenter.onPageScrolled()
        verify<View>(mockView).setNextEnabled(true)
    }

    private fun makeCompletedBallot() {
        val completeBallot = EntryBallot(UUID.randomUUID())
        `when`(mockView.entryBallot).thenReturn(completeBallot)
        completeBallot.addScore(Score(mockCategory, 1))

        val completedBallots = ArrayList<EntryBallot>()
        completedBallots.add(completeBallot)
        `when`(mockView.allBallots).thenReturn(completedBallots)
    }

    private fun makeIncompleteBallots() {
        val incompleteBallot = EntryBallot(UUID.randomUUID()).apply {
            addScore(Score(Category("TEST", "TEST"), 0))
            addScore(Score(Category("TESTER", "TEST"), 0))
        }

        `when`(mockView.entryBallot).thenReturn(incompleteBallot)
        `when`(mockView.allBallots).thenReturn(listOf(incompleteBallot))
    }
}
