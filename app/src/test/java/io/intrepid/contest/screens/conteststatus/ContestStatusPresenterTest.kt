package io.intrepid.contest.screens.conteststatus

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.intrepid.contest.BuildConfig
import io.intrepid.contest.models.Contest
import io.intrepid.contest.models.ParticipationType
import io.intrepid.contest.rest.ContestStatus
import io.intrepid.contest.rest.ContestStatusResponse
import io.intrepid.contest.rest.ContestWrapper
import io.intrepid.contest.screens.conteststatus.ContestStatusContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable
import io.reactivex.Observable.error
import io.reactivex.functions.Consumer
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.*
import java.util.*
import java.util.concurrent.TimeUnit

class ContestStatusPresenterTest : BasePresenterTest<ContestStatusPresenter>() {

    /*
     * API call interval is shorter for dev builds, a little longer for QA builds, and longest for release
     */
    private val API_CALL_INTERVAL = if (BuildConfig.DEV_BUILD) 3 else if (BuildConfig.DEBUG) 10 else 2
    private val API_CALL_INTERVAL_UNIT = if (BuildConfig.DEBUG) TimeUnit.SECONDS else TimeUnit.MINUTES

    @Mock
    private lateinit var mockView: View

    private lateinit var throwable: Throwable

    @Before
    fun setup() {
        throwable = Throwable()
        presenter = ContestStatusPresenter(mockView, testConfiguration, false)

        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
    }

    private fun getContestStatusResponseWaitingForSubmissions() {
        val response = ContestStatusResponse().apply {
            contestStatus = ContestStatus()
            contestStatus.setSubmissionData(false, 0, 5)
            contestStatus.setJudgeData(false, 0, 1)
        }
        `when`(mockRestApi.getContestStatus(any<String>())).thenReturn(Observable.just(response))
    }

    private fun getContestStatusResponseWaitingForScores() {
        val response = ContestStatusResponse().apply {
            contestStatus = ContestStatus()
            contestStatus.setSubmissionData(false, 5, 5)
            contestStatus.setJudgeData(false, 0, 1)
        }
        `when`(mockRestApi.getContestStatus(any<String>())).thenReturn(Observable.just(response))
    }

    private fun getContestStatusResponseResultsAvailable() {
        val response = ContestStatusResponse().apply {
            contestStatus = ContestStatus()
            contestStatus.setSubmissionData(true, 5, 5)
            contestStatus.setJudgeData(true, 1, 1)
        }
        `when`(mockRestApi.getContestStatus(any<String>())).thenReturn(Observable.just(response))
    }

    @Test
    fun onViewCreatedShouldShowAdminStatusPageIfParticipationTypeIsCreator() {
        getContestStatusResponseWaitingForSubmissions()
        `when`(mockPersistentSettings.currentParticipationType).thenReturn(ParticipationType.CREATOR)

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showAdminStatusPage()
    }

    @Test
    fun onViewCreatedShouldShowResultsWhenJudgeHasSubmittedScoreAndResultsAreAvailable() {
        getContestStatusResponseResultsAvailable()
        presenter = ContestStatusPresenter(mockView, testConfiguration, true)
        `when`(mockPersistentSettings.currentParticipationType).thenReturn(ParticipationType.JUDGE)

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showResultsAvailableFragment()
    }

    @Test
    fun onViewCreatedShouldShowStatusWaitingFragmentIfJudgeJustVoted() {
        getContestStatusResponseWaitingForScores()
        `when`(mockPersistentSettings.currentParticipationType).thenReturn(ParticipationType.JUDGE)

        presenter = ContestStatusPresenter(mockView, testConfiguration, true)
        presenter.onViewCreated()

        testConfiguration.triggerRxSchedulers()
        verify<View>(mockView).showStatusWaitingFragment()
    }

    @Test
    fun onViewCreatedShouldShowStatusWaitingFragmentWhenStatusIsWaitingForSubmissions() {
        getContestStatusResponseWaitingForSubmissions()
        `when`(mockPersistentSettings.currentParticipationType).thenReturn(ParticipationType.CONTESTANT)

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showStatusWaitingFragment()
        verify<View>(mockView, never()).showResultsAvailableFragment()
        verify<View>(mockView, never()).showContestOverviewPage()
    }

    @Test
    fun onViewCreatedShouldShowStatusWaitingFragmentWhenWaitingForScoresAndParticipantIsContestant() {
        getContestStatusResponseWaitingForScores()
        `when`(mockPersistentSettings.currentParticipationType).thenReturn(ParticipationType.CONTESTANT)

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showStatusWaitingFragment()
        verify<View>(mockView, never()).showResultsAvailableFragment()
        verify<View>(mockView, never()).showContestOverviewPage()
    }

    @Test
    fun onViewCreatedShouldShowContestOverviewWhenWaitingForScoresAndParticipantIsJudge() {
        `when`(mockPersistentSettings.currentParticipationType).thenReturn(ParticipationType.JUDGE)
        getContestStatusResponseWaitingForScores()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showContestOverviewPage()
        verify<View>(mockView, never()).showStatusWaitingFragment()
        verify<View>(mockView, never()).showResultsAvailableFragment()
    }

    @Test
    fun onViewCreatedShouldShowResultsAvailableFragmentWhenStatusIsResultsAvailable() {
        `when`(mockPersistentSettings.currentParticipationType).thenReturn(ParticipationType.CONTESTANT)
        getContestStatusResponseResultsAvailable()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showResultsAvailableFragment()
        verify<View>(mockView, never()).showStatusWaitingFragment()
        verify<View>(mockView, never()).showContestOverviewPage()
    }

    @Test
    fun onViewCreatedShouldRefreshWaitingSubmissionsFragmentPeriodically() {
        getContestStatusResponseWaitingForSubmissions()
        `when`(mockPersistentSettings.currentParticipationType).thenReturn(ParticipationType.CONTESTANT)

        presenter.onViewCreated()
        testConfiguration.ioScheduler.advanceTimeBy(API_CALL_INTERVAL.toLong(), API_CALL_INTERVAL_UNIT)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView, times(2)).showStatusWaitingFragment()
    }

    @Test
    fun onViewCreatedShouldRefreshOverviewPageWhenParticipationTypeIsJudge() {
        getContestStatusResponseWaitingForSubmissions()
        `when`(mockPersistentSettings.currentParticipationType).thenReturn(ParticipationType.JUDGE)

        presenter.onViewCreated()
        testConfiguration.ioScheduler.advanceTimeBy(API_CALL_INTERVAL.toLong(), API_CALL_INTERVAL_UNIT)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView, times(2)).showContestOverviewPage()
    }

    @Test
    @Throws(HttpException::class)
    fun onViewCreatedShouldShowApiErrorMessageWhenApiCallThrowsError() {
        `when`(mockRestApi.getContestStatus(any<String>())).thenReturn(error<ContestStatusResponse>(throwable))

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(anyInt())
    }

    @Test
    fun onBackPressedShouldCauseViewToReturnToSplashScreen() {
        presenter.onBackPressed()
        verify<View>(mockView).returnToSplashScreen()
    }

    @Test
    @Throws(Exception::class)
    fun onRequestContestDetailsShouldCallResponseConsumerWhenApiCallDoesNotThrowError() {
        val responseConsumer = mock<Consumer<ContestWrapper>>()
        val throwableConsumer = mock<Consumer<Throwable>>()
        `when`(mockRestApi.getContestDetails(any<String>())).thenReturn(Observable.just(ContestWrapper(Contest())))

        presenter.requestContestDetails(responseConsumer, throwableConsumer)
        testConfiguration.triggerRxSchedulers()

        verify<Consumer<ContestWrapper>>(responseConsumer).accept(any<ContestWrapper>())
        verify<Consumer<Throwable>>(throwableConsumer, never()).accept(any(Throwable::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun onRequestContestDetailsShouldCallThrowableConsumerWhenApiCallThrowsError() {
        val responseConsumer = mock<Consumer<ContestWrapper>>()
        val throwableConsumer = mock<Consumer<Throwable>>()
        `when`(mockRestApi.getContestDetails(any<String>())).thenReturn(error<ContestWrapper>(throwable))

        presenter.requestContestDetails(responseConsumer, throwableConsumer)
        testConfiguration.triggerRxSchedulers()

        verify<Consumer<Throwable>>(throwableConsumer).accept(any(Throwable::class.java))
        verify<Consumer<ContestWrapper>>(responseConsumer, never()).accept(any<ContestWrapper>())
    }
}
