package io.intrepid.contest.screens.contestresults

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.intrepid.contest.models.RankedEntryResult
import io.intrepid.contest.rest.ContestResultResponse
import io.intrepid.contest.screens.contestresults.ContestResultsContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable
import io.reactivex.Observable.error
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.util.*

class ContestResultsPresenterTest : BasePresenterTest<ContestResultsPresenter>() {
    @Mock
    private lateinit var mockView: View

    @Before
    fun setup() {
        presenter = ContestResultsPresenter(mockView, testConfiguration)
    }

    @Test
    fun onViewCreatedShouldHideNoEntriesMessageWhenSuccessfullyRetrievedResultsWithEntries() {
        setupSuccessfulContestResultsCall(validContestResultResponse)

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).hideNoEntriesMessage()
    }

    @Test
    fun onViewCreatedShouldShowResultsWhenSuccessfullyRetrievedResultsWithEntries() {
        val resultResponse = validContestResultResponse
        setupSuccessfulContestResultsCall(resultResponse)

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showResults(resultResponse.contestResults.rankedScoredEntries)
    }

    @Test
    fun onViewCreatedShouldShowNeverHideNoEntriesMessageWhenSuccessfullyRetrievedResultsWithoutEntries() {
        setupSuccessfulContestResultsCall(ContestResultResponse())

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView, never()).hideNoEntriesMessage()
    }

    @Test
    fun onViewCreatedShouldShowNeverShowResultsWhenSuccessfullyRetrievedResultsWithoutEntries() {
        setupSuccessfulContestResultsCall(ContestResultResponse())

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView, never()).showResults(anyList<RankedEntryResult>())
    }

    @Test
    @Throws(HttpException::class)
    fun onViewCreatedShouldShowApiErrorMessageWhenItemIsSendInvitationsAndApiCallThrowsError() {
        setupUnsuccessfulContestResultsCall()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(anyInt())
    }

    private fun setupUnsuccessfulContestResultsCall() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.getContestResults(any<String>())).thenReturn(error<ContestResultResponse>(Throwable()))
    }

    private fun setupSuccessfulContestResultsCall(resultResponse: ContestResultResponse) {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.getContestResults(any<String>())).thenReturn(Observable.just(resultResponse))
    }

    private val validContestResultResponse: ContestResultResponse
        get() {
            return ContestResultResponse(listOf(RankedEntryResult(), RankedEntryResult(), RankedEntryResult()))
        }
}
