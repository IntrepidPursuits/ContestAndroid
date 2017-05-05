package io.intrepid.contest.screens.contestcreation

import android.support.annotation.StringRes
import io.intrepid.contest.R
import io.intrepid.contest.models.Category
import io.intrepid.contest.models.Contest
import io.intrepid.contest.rest.ContestWrapper
import io.intrepid.contest.screens.contestcreation.NewContestMvpContract.View
import io.intrepid.contest.screens.contestcreation.reviewcontest.ReviewContestFragment
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import java.util.*

class NewContestPresenterTest : BasePresenterTest<NewContestPresenter>() {

    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockChildFragment: ContestCreationFragment
    @Mock
    private lateinit var mockReviewContestFragment: ReviewContestFragment
    @Mock
    private lateinit var mockContestBuilder: Contest.Builder

    private lateinit var categories: MutableList<Category>

    @Before
    fun setup() {
        categories = ArrayList<Category>()
        for (i in 0..2) {
            categories.add(Category("TEST TITLE " + i, "TEST DESCRIPTION " + i))
        }
        presenter = NewContestPresenter(mockView, testConfiguration)
        presenter.contest = mockContestBuilder
        presenter.onViewCreated()
    }

    @Test
    fun presenterCanBeCreatedSuccessfully() {
        assertTrue(presenter.getContest() != null)
        verify<View>(mockView).showContestSubmissionPage(0)
    }

    @Test
    fun onBackButtonClickedFromFirstPageShouldCancelEdit() {
        presenter.onBackButtonClicked()
        verify<View>(mockView).cancelEdit()
    }

    @Test
    fun onBackButtonClickedShouldNavigateBackwards() {
        `when`(mockView.currentIndex).thenReturn(2)
        presenter.onBackButtonClicked()
        verify<View>(mockView).showContestSubmissionPage(1)
    }

    @Test
    fun onNextButtonClickedShouldNavigateForward() {
        `when`(mockView.getChildEditFragment(0)).thenReturn(mockChildFragment)
        mockView.showContestSubmissionPage(0)

        presenter.onNextButtonClicked()

        verify<ContestCreationFragment>(mockChildFragment).onNextClicked()
    }

    @Test
    fun onNextDisabledShouldCauseViewToHideNextButton() {
        presenter.onNextStatusChanged(false)
        verify<View>(mockView).setNextVisible(false)
    }

    @Test
    fun onNextEnabledShouldCauseViewToShowNextButton() {
        presenter.onNextStatusChanged(true)
        verify<View>(mockView).setNextVisible(true)
    }

    @Test
    fun showNextScreenShouldTriggerViewToShowNextScreen() {
        `when`(mockView.currentIndex).thenReturn(1)
        presenter.showNextScreen()
        verify<View>(mockView).showContestSubmissionPage(2)
    }

    @Test
    fun onNewCategoryAddedShouldCauseViewToShowUpdatedCategories() {
        presenter.onNewCategoryAdded("Category name", "Category description")
        verify<View>(mockView).showUpdatedCategories()
    }

    @Test
    fun showAddCategoryScreenShouldCauseViewToNavigateToAddCategoryPage() {
        presenter.showAddCategoryScreen()
        verify<View>(mockView).navigateToAddCategoryPage(eq(presenter.getContest()))
    }

    @Test
    fun onEditContestCaegoryShouldTriggerViewToUpdateCategory() {
        `when`(mockContestBuilder.getCategories()).thenReturn(categories)
        presenter.onContestEditEntered(0, "New Name", "New Description")
        verify<View>(mockView).showUpdatedCategories()
    }

    @Test
    fun onPageSelectedShouldCauseViewToShowCorrectPageTitle() {
        testOnPageScrolledWithExpectedPageTitleStringResource(0, R.string.new_contest)
        testOnPageScrolledWithExpectedPageTitleStringResource(1, R.string.description)
        testOnPageScrolledWithExpectedPageTitleStringResource(2, R.string.scoring_categories)
        testOnPageScrolledWithExpectedPageTitleStringResource(3, R.string.review_contest)
    }

    @Test
    fun onPageChangedToReviewContestViewShouldTriggerViewToDoOnFocus() {
        `when`(mockView.getChildEditFragment(anyInt())).thenReturn(mockReviewContestFragment)
        presenter.onPageSelected(anyInt())
        verify<ReviewContestFragment>(mockReviewContestFragment).onPageSelected()
    }

    @Test
    fun submitContestSuccessfullyShouldTriggerViewToNavigateToInviteScreen() {
        val contestResponse = ContestWrapper(Contest())
        `when`(mockRestApi.submitContest(any<ContestWrapper>())).thenReturn(Observable.just(contestResponse))
        `when`(mockView.currentIndex).thenReturn(NewContestPresenter.LAST_PAGE_INDEX)

        presenter.showNextScreen()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).navigateToSendInvitationsScreen()
    }

    @Test
    fun submitContestFailureShouldTriggerViewToShowErrorMessage() {
        `when`(mockRestApi.submitContest(any<ContestWrapper>())).thenReturn(Observable.error<ContestWrapper>(Throwable()))
        `when`(mockView.currentIndex).thenReturn(NewContestPresenter.LAST_PAGE_INDEX)

        presenter.showNextScreen()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(R.string.error_api)
    }

    private fun testOnPageScrolledWithExpectedPageTitleStringResource(pageToNavigateTo: Int, @StringRes pageTitle: Int) {
        `when`(mockView.getChildEditFragment(anyInt())).thenReturn(mockChildFragment)
        presenter.onPageSelected(pageToNavigateTo)
        verify<View>(mockView).setPageTitle(pageTitle)
    }
}
