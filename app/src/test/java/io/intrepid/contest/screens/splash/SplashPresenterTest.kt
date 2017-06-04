package io.intrepid.contest.screens.splash

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.intrepid.contest.R
import io.intrepid.contest.models.ActiveContestListResponse
import io.intrepid.contest.models.Contest
import io.intrepid.contest.models.ParticipationType
import io.intrepid.contest.models.User
import io.intrepid.contest.rest.UserCreationResponse
import io.intrepid.contest.screens.splash.SplashContract.View
import io.intrepid.contest.settings.PersistentSettings
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable
import io.reactivex.Observable.error
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class SplashPresenterTest : BasePresenterTest<SplashPresenter>() {
    @Mock
    private lateinit var mockView: View

    @Before
    fun setup() {
        presenter = SplashPresenter(mockView, testConfiguration)
    }

    @Test
    fun onViewCreatedShouldShowUserButtonsWhenUserCreationIsSuccessful() {
        `when`(mockRestApi.activeContests).thenReturn(Observable.just(createActiveContestListResponse()))
        setupSuccessfulUserCreation()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showUserButtons()
    }

    @Test
    fun onViewCreatedShouldShowUserButtonsWhenUserIsRetrievedFromPersistentSettings() {
        `when`(mockRestApi.activeContests).thenReturn(Observable.just(createActiveContestListResponse()))
        `when`(mockPersistentSettings.isAuthenticated).thenReturn(true)

        presenter.onViewCreated()

        verify<View>(mockView).showUserButtons()
    }

    @Test
    @Throws(HttpException::class)
    fun onViewCreatedShouldShowApiErrorMessageWhenApiCallThrowsError() {
        setupFailedUserCreation()

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(any(Int::class.javaPrimitiveType!!))
    }

    @Test
    fun createContestClicked() {
        presenter.onCreateContestClicked()
        verify<View>(mockView).showCreateContestScreen()
    }

    @Test
    fun joinContestClicked() {
        presenter.onJoinContestClicked()
        verify<View>(mockView).showJoinContestScreen()
    }

    @Test
    fun onViewCreatedShouldCauseViewToShowOngoingContestsWhenUserIsAuthenticated() {
        `when`(mockPersistentSettings.isAuthenticated).thenReturn(true)
        `when`(mockRestApi.activeContests).thenReturn(Observable.just(createActiveContestListResponse()))

        presenter.onViewCreated()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showOngoingContests(anyList<Contest>())
    }

    @Test
    fun onViewCreatedShouldCauseViewToShowOngoingContestsAfterAuthenticateUserSuccess() {
        doSuccessfulAuthenticationApiCallAndActiveContestsCallInSequence(true)
        verify<View>(mockView).showOngoingContests(anyList<Contest>())
    }

    @Test
    fun onViewCreatedShouldShowErrorWhenDiscoverOngoingContestReturnsThrowable() {
        doSuccessfulAuthenticationApiCallAndActiveContestsCallInSequence(false)
        verify<View>(mockView).showMessage(R.string.error_api)
    }

    @Test
    fun onContestClickedShouldCauseViewToResumeContest() {
        val contest = Contest()
        presenter.onContestClicked(contest)
        verify<View>(mockView).resumeContest(contest)
    }

    @Test
    fun onContestClickedShouldSaveParticipationTypeAsCreatorWhenApiParticipationIsNull() {
        val contest = Contest().apply {
            id = UUID.randomUUID()
            participationType = null
        }

        presenter.onContestClicked(contest)

        verify<PersistentSettings>(mockPersistentSettings).setCurrentParticipationType(ParticipationType.CREATOR)
    }

    @Test
    fun onContestClickedShouldSaveParticipationTypeAsContestantWhenApiParticipationIsContestant() {
        val contest = Contest().apply {
            id = UUID.randomUUID()
            participationType = ParticipationType.CONTESTANT
        }

        presenter.onContestClicked(contest)

        verify<PersistentSettings>(mockPersistentSettings).setCurrentParticipationType(ParticipationType.CONTESTANT)
    }

    @Test
    fun onContestClickedShouldSaveParticipationTypeAsJudgeWhenApiParticipationIsJudge() {
        val contest = Contest().apply {
            id = UUID.randomUUID()
            participationType = ParticipationType.JUDGE
        }

        presenter.onContestClicked(contest)

        verify<PersistentSettings>(mockPersistentSettings).setCurrentParticipationType(ParticipationType.JUDGE)
    }

    private fun createActiveContestListResponse(): ActiveContestListResponse {
        return ActiveContestListResponse(emptyList())
    }

    private fun doSuccessfulAuthenticationApiCallAndActiveContestsCallInSequence(isActiveContestCallSuccessful: Boolean) {
        val response = if (isActiveContestCallSuccessful)
            Observable.just(createActiveContestListResponse())
        else
            error<ActiveContestListResponse>(Throwable())
        `when`(mockRestApi.activeContests).thenReturn(response)
        setupSuccessfulUserCreation()

        presenter.onViewCreated()
        //Called twice to trigger second api call.
        testConfiguration.triggerRxSchedulers()
        testConfiguration.triggerRxSchedulers()
    }

    private fun setupSuccessfulUserCreation() {
        val UserCreationResponse = UserCreationResponse().apply {
            user = User()
            user.id = UUID.randomUUID()
        }
        `when`(mockRestApi.createUser()).thenReturn(Observable.just(UserCreationResponse))
    }

    private fun setupFailedUserCreation() {
        `when`(mockRestApi.createUser()).thenReturn(error<UserCreationResponse>(Throwable()))
    }
}
