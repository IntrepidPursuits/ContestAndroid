package io.intrepid.contest.screens.join

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.intrepid.contest.models.Participant
import io.intrepid.contest.models.ParticipationType
import io.intrepid.contest.rest.RedeemInvitationRequest
import io.intrepid.contest.rest.RedeemInvitationResponse
import io.intrepid.contest.screens.join.JoinContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable
import io.reactivex.Observable.error
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class JoinPresenterTest : BasePresenterTest<JoinPresenter>() {

    private val CODE = "CODE"
    private val JUDGE_CODE = "judge"
    private val VALID_POTENTIAL_CODE = "KbUEcko"
    private val INVALID_POTENTIAL_CODE = "kbuecko"

    @Mock
    internal lateinit var mockView: View

    private lateinit var throwable: Throwable

    @Before
    fun setup() {
        throwable = Throwable()
        presenter = JoinPresenter(mockView, testConfiguration)
    }

    @Test
    fun onEntryCodeTextViewChangedShouldHideSubmitButtonWhenCodeIsEmpty() {
        val newCode = ""

        presenter.onEntryCodeTextChanged(newCode)

        verify<View>(mockView).hideSubmitButton()
        verify<View>(mockView, never()).showSubmitButton()
    }

    @Test
    fun onEntryCodeTextViewChangedShouldShowSubmitButtonWhenCodeIsNotEmpty() {
        val newCode = "1"

        presenter.onEntryCodeTextChanged(newCode)

        verify<View>(mockView).showSubmitButton()
        verify<View>(mockView, never()).hideSubmitButton()
    }

    @Test
    fun onSubmitButtonClickedShouldShowEntryNameScreenWhenCodeIsValidForContestant() {
        val redeemInvitationResponse = RedeemInvitationResponse().apply {
            participant = Participant()
            participant.contestId = UUID.randomUUID()
            participant.participationType = ParticipationType.CONTESTANT
        }
        `when`(mockRestApi.redeemInvitationCode(any<String>(), any<RedeemInvitationRequest>()))
                .thenReturn(Observable.just(redeemInvitationResponse))

        presenter.onSubmitButtonClicked(CODE)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showEntryNameScreen()
    }

    @Test
    fun onSubmitButtonClickedShouldShowContestStatusScreenWhenCodeIsValidForJudge() {
        val redeemInvitationResponse = RedeemInvitationResponse().apply {
            participant = Participant()
            participant.contestId = UUID.randomUUID()
            participant.participationType = ParticipationType.JUDGE
        }
        `when`(mockRestApi.redeemInvitationCode(any<String>(), any<RedeemInvitationRequest>()))
                .thenReturn(Observable.just(redeemInvitationResponse))

        presenter.onSubmitButtonClicked(JUDGE_CODE)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showContestStatusScreen()
    }

    @Test
    fun onSubmitButtonClickedShouldShowInvalidCodeErrorMessageWhenCodeIsInvalid() {
        val mockRedeemInvitationResponse = mock(RedeemInvitationResponse::class.java)
        `when`(mockRestApi.redeemInvitationCode(any<String>(), any<RedeemInvitationRequest>()))
                .thenReturn(Observable.just(mockRedeemInvitationResponse))

        presenter.onSubmitButtonClicked(CODE)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showInvalidCodeErrorMessage()
    }

    @Test
    fun onSubmitButtonClickedShouldShowInvalidCodeErrorMessageWhenApiCallThrowsNotFoundError() {
        val notFoundException = HttpException(Response.error<Any>(
                HttpURLConnection.HTTP_NOT_FOUND,
                ResponseBody.create(MediaType.parse("application/json"),
                        "{\"errors\":[\"Couldn't find Invitation\"]}")))
        `when`(mockRestApi.redeemInvitationCode(any<String>(), any<RedeemInvitationRequest>()))
                .thenReturn(error<RedeemInvitationResponse>(notFoundException))

        presenter.onSubmitButtonClicked(CODE)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showInvalidCodeErrorMessage()
    }

    @Test
    @Throws(HttpException::class)
    fun onSubmitButtonClickedShouldShowApiErrorMessageWhenApiCallThrowsUntreatedErrors() {
        `when`(mockRestApi.redeemInvitationCode(any<String>(), any<RedeemInvitationRequest>()))
                .thenReturn(error<RedeemInvitationResponse>(throwable))

        presenter.onSubmitButtonClicked(CODE)
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(any(Int::class.javaPrimitiveType!!))
    }

    @Test
    fun onBackPressedShouldCauseViewToCancelJoinContest() {
        presenter.onBackPressed()
        verify<View>(mockView).cancelJoinContest()
    }

    @Test
    fun onViewBoundShouldCauseViewToShowClipboardDataWhenClipboardDataIsValid() {
        `when`(mockView.lastCopiedText).thenReturn(VALID_POTENTIAL_CODE)
        presenter.onViewBound()
        verify<View>(mockView).showClipboardData(VALID_POTENTIAL_CODE)
    }

    @Test
    fun onViewBoundShouldCauseViewToDoNothingWhenClipboardDataIsInvalid() {
        `when`(mockView.lastCopiedText).thenReturn(INVALID_POTENTIAL_CODE)
        presenter.onViewBound()
        verify<View>(mockView, never()).showClipboardData(INVALID_POTENTIAL_CODE)
    }
}
