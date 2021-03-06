package io.intrepid.contest.screens.contestoverview

import org.junit.Before
import org.junit.Test
import org.mockito.Mock

import java.util.ArrayList
import java.util.UUID

import io.intrepid.contest.R
import io.intrepid.contest.models.Category
import io.intrepid.contest.models.Contest
import io.intrepid.contest.models.Entry
import io.intrepid.contest.models.ScoreWeight
import io.intrepid.contest.rest.ContestWrapper
import io.intrepid.contest.screens.contestoverview.ContestOverviewContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable

import io.reactivex.Observable.error
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class ContestOverviewPresenterTest : BasePresenterTest<ContestOverviewPresenter>() {
    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockContest: Contest

    @Before
    fun setup() {
        presenter = ContestOverviewPresenter(mockView, testConfiguration)

        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
    }

    private fun setupSuccessfulContestDetailsCall() {
        val response = ContestWrapper(mockContest)
        `when`(mockRestApi.getContestDetails(any<String>())).thenReturn(Observable.just(response))
    }

    private fun setupFailedContestDetailsCall() {
        `when`(mockRestApi.getContestDetails(any<String>())).thenReturn(error<ContestWrapper>(Throwable()))
    }

    @Test
    fun onViewCreatedShouldShowErrorOnContestDetailCallFailure() {
        setupFailedContestDetailsCall()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(R.string.error_api)
    }

    @Test
    @Throws(Exception::class)
    fun onViewCreatedShouldShowRatingGuide() {
        //Not a pre-condition for this test but is required to make sure onViewCreated does not fail
        setupSuccessfulContestDetailsCall()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showCategoriesAndWeights(any<List<Category>>(), any<List<ScoreWeight>>())
    }

    @Test
    @Throws(Exception::class)
    fun showContestDescription() {
        setupSuccessfulContestDetailsCall()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showContestDescription(any<String>())
    }

    @Test
    fun onViewCreatedShouldShowCategories() {
        setupSuccessfulContestDetailsCall()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showCategoriesAndWeights(anyList<Category>(), anyList<ScoreWeight>())
    }

    @Test
    fun onViewCreatedShouldCauseVIewToShowContestTitle() {
        `when`<List<Entry>>(mockContest.entries).thenReturn(ArrayList<Entry>())
        setupSuccessfulContestDetailsCall()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showTitle(anyInt(), any<String>())
    }

    @Test
    fun onViewCreatedShouldCauseViewToShowSubmissionCountMessage() {
        setupSuccessfulContestDetailsCall()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showSubmissionCountMessage(anyInt(), anyInt())
    }

    @Test
    @Throws(Exception::class)
    fun onViewCreatedShouldShowApiErrorMessageWhenContestDetailsCallThrowsError() {
        setupFailedContestDetailsCall()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(any(Int::class.javaPrimitiveType!!))
    }

    @Test
    fun onOverviewSubmitButtonClickedShouldOpenJudgingScreen() {
        setupSuccessfulContestDetailsCall()
        presenter.onOverViewSubmitButtonClicked()
        verify<View>(mockView).advanceToJudgingScreen()
    }

    @Test
    fun onBackPressedShouldCauseViewToReturnToSplashScreen() {
        presenter.onBackPressed()
        verify<View>(mockView).returnToSplashScreen()
    }
}
