package io.intrepid.contest.screens.contestcreation.reviewcontest

import io.intrepid.contest.models.Contest
import io.intrepid.contest.screens.contestcreation.reviewcontest.ReviewContestContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ReviewContestPresenterTest : BasePresenterTest<ReviewContestPresenter>() {
    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockContestBuilder: Contest.Builder
    @Mock
    private lateinit var mockContest: Contest

    @Before
    fun setup() {
        presenter = ReviewContestPresenter(mockView,
                testConfiguration,
                mockContestBuilder)
    }

    @Test
    fun onViewCreatedShouldTriggerViewToShowReviewPage() {
        presenter.onViewCreated()
        verify<View>(mockView).displayReviewPageContent(mockContestBuilder)
    }

    @Test
    fun onContestTitleSelectedShouldCauseViewToShowEditTitlePage() {
        presenter.onContestTitleSelected()
        verify<View>(mockView).showEditTitlePage(mockContestBuilder)
    }

    @Test
    fun onContestDescriptionSelectedShouldCauseViewToShowEditDescriptionPage() {
        presenter.onContestDescriptionSelected()
        verify<View>(mockView).showEditDescriptionPage(mockContestBuilder)
    }

    @Test
    fun onPageSelectedShouldDisplayReviewPageContent() {
        presenter.onPageSelected()
        verify<View>(mockView).displayReviewPageContent(mockContestBuilder)
    }
}

