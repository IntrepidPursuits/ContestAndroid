package io.intrepid.contest.screens.adminstatus

import org.junit.Before
import org.junit.Test
import org.mockito.Mock

import java.util.UUID

import io.intrepid.contest.R
import io.intrepid.contest.models.Contest
import io.intrepid.contest.models.Entry
import io.intrepid.contest.rest.ContestStatus
import io.intrepid.contest.rest.ContestStatusResponse
import io.intrepid.contest.rest.ContestWrapper
import io.intrepid.contest.screens.adminstatus.AdminStatusContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable

import io.intrepid.contest.screens.adminstatus.ConfirmStartContestDialog.AdminActionType.END_CONTEST
import io.intrepid.contest.screens.adminstatus.ConfirmStartContestDialog.AdminActionType.START_CONTEST
import io.reactivex.Observable.error
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class AdminStatusPresenterTest : BasePresenterTest<AdminStatusPresenter>() {

    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockStatus: ContestStatus

    @Before
    fun setup() {
        presenter = AdminStatusPresenter(mockView, testConfiguration)
    }

    private fun setupSuccessfulContestStatusApiResponse() {
        `when`(mockRestApi.getContestDetails(any<String>())).thenReturn(Observable.just(ContestWrapper(Contest())))

        val response = ContestStatusResponse()
        response.contestStatus = mockStatus
        `when`(mockRestApi.getContestStatus(any<String>())).thenReturn(Observable.just(response))
    }

    @Test
    fun onViewCreatedShouldCauseViewToShowAwaitSubmissionsIndicator() {
        setupSuccessfulContestStatusApiResponse()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showAwaitSubmissionsIndicator(anyBoolean())
    }

    @Test
    fun onBottonNavigationButtonClickedShouldCauseToAdvanceToJudging() {
        setupSuccessfulCloseSubmissionsApiCall()
        presenter.onBottomNavigationButtonClicked()

        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).advanceToJudgingIndicator()
    }

    @Test
    fun onViewCreatedShouldCauseViewToShowSubmittedEntries() {
        setupSuccessfulContestStatusApiResponse()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showSubmittedEntries(any<List<Entry>>())
    }

    @Test
    fun onViewCreatedShouldCauseViewToShowErrorMessageWhenGetContestDetailsReturnsThrowable() {
        setupSuccessfulContestStatusApiResponse()
        `when`(mockRestApi.getContestDetails(any<String>())).thenReturn(error<ContestWrapper>(Throwable()))

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(R.string.error_api)
    }

    @Test
    fun onViewCreatedShouldCauseViewToShowErrorMessageWhenGetContestStatusReturnsThrowable() {
        setupSuccessfulContestStatusApiResponse()
        `when`(mockRestApi.getContestStatus(any<String>())).thenReturn(error<ContestStatusResponse>(Throwable()))

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(R.string.error_api)
    }

    @Test
    fun onBottomNavigationButtonClickedShouldCauseViewToShowConfirmStartContestDialog() {
        setupSuccessfulContestStatusApiResponse()
        `when`(mockStatus.numSubmissionsMissing).thenReturn(6)
        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        presenter.onBottomNavigationButtonClicked()

        verify<View>(mockView).showConfirmStartContestDialog()
        verify<View>(mockView, never()).advanceToJudgingIndicator()
    }

    private fun setupSuccessfulCloseSubmissionsApiCall() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.closeSubmissions(anyString())).thenReturn(Observable.just(ContestWrapper(Contest())))
    }

    @Test
    fun onBottomNavigationButtonClickedShouldCauseViewToAdvanceJudgingIndicatorIfNoMissingEntries() {
        setupSuccessfulCloseSubmissionsApiCall()
        presenter.onBottomNavigationButtonClicked()
        testConfiguration.triggerRxSchedulers()
        verify<View>(mockView).advanceToJudgingIndicator()
    }

    @Test
    fun onBottomNavClickedTwiceShouldCauseViewToShowConfirmEndContestDialogIfMissingJudges() {
        setupSuccessfulCloseSubmissionsApiCall()
        `when`(mockStatus.numScoresMissing).thenReturn(5)
        setupSuccessfulContestStatusApiResponse()
        presenter.onViewCreated()

        //Click to start contest
        presenter.onBottomNavigationButtonClicked()
        testConfiguration.triggerRxSchedulers()
        //Then click to end contest
        presenter.onBottomNavigationButtonClicked()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showConfirmEndContestDialog()
    }

    @Test
    fun failureEndingContestShouldCauseViewToShowErrorMessage() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.endContest(anyString())).thenReturn(error<ContestWrapper>(Throwable()))

        presenter.onPositiveButtonClicked(END_CONTEST)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(R.string.error_api)
    }

    @Test
    fun onBottomNavClickedTwiceShouldCauseViewToAdvanceToEndOfContestIndicatorWhenNoMissingJudges() {
        setupSuccessfulCloseSubmissionsApiCall()
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.endContest(anyString())).thenReturn(Observable.just(ContestWrapper(Contest())))

        presenter.onBottomNavigationButtonClicked()
        testConfiguration.triggerRxSchedulers()
        presenter.onBottomNavigationButtonClicked()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showJudgingStatusIndicator(false)
        verify<View>(mockView).showEndOfContestIndicator(true)
    }

    @Test
    fun onPositiveButtonClickedOnStartContestDialogShouldCauseViewToCloseSubmissions() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.closeSubmissions(anyString())).thenReturn(Observable.just(ContestWrapper(Contest())))
        presenter.onPositiveButtonClicked(START_CONTEST)

        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).advanceToJudgingIndicator()
    }

    @Test
    fun onPositiveClickedOnStartContestDialogShouldCauseViewToShowErrorMsgIfErrorOccurs() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.closeSubmissions(anyString())).thenReturn(error<ContestWrapper>(Throwable()))

        presenter.onPositiveButtonClicked(START_CONTEST)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(R.string.error_api)
    }

    @Test
    fun onPositiveButtonClickedOnEndContestDialogShouldCauseViewToAdvanceToEndOfContestIndicator() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.endContest(anyString())).thenReturn(Observable.just(ContestWrapper(Contest())))

        presenter.onPositiveButtonClicked(END_CONTEST)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showJudgingStatusIndicator(false)
        verify<View>(mockView).showEndOfContestIndicator(true)
    }

    @Test
    fun onNegativeButtonClickedShouldDismissDialogAndDoNothing() {
        presenter.onNegativeButtonClicked(START_CONTEST)
        presenter.onNegativeButtonClicked(END_CONTEST)

        verify<View>(mockView, never()).showJudgingStatusIndicator(anyBoolean())
        verify<View>(mockView, never()).showEndOfContestIndicator(anyBoolean())
    }

    @Test
    fun onBackPressedShouldCauseViewToExitAdminStatusScreen() {
        presenter.onBackPressed()
        verify<View>(mockView).exitStatusScreen()
    }
}
