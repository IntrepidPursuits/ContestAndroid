package io.intrepid.contest.screens.sendinvitations.invitationintro

import io.intrepid.contest.R
import io.intrepid.contest.models.ParticipationType
import io.intrepid.contest.screens.sendinvitations.invitationintro.InvitationIntroContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.verify

class InvitationRequestIntroPresenterTest : BasePresenterTest<InvitationIntroPresenter>() {
    @Mock
    private lateinit var mockView: View

    private fun setup(hasContactPermissions: Boolean, participationType: ParticipationType) {
        presenter = InvitationIntroPresenter(mockView, testConfiguration, hasContactPermissions, participationType)
    }

    @Test
    fun onViewBoundShouldShowSelectContestantsMessageWhenAppHasPermissionsAndIsInvitingContestants() {
        setup(true, ParticipationType.CONTESTANT)
        presenter.onViewBound()
        verify<View>(mockView).showSelectContactsMessage(R.string.invite_contestants_intro)
    }

    @Test
    fun onViewBoundShouldShowSelectJudgesMessageWhenAppHasPermissionsAndIsInvitingJudges() {
        setup(true, ParticipationType.JUDGE)
        presenter.onViewBound()
        verify<View>(mockView).showSelectContactsMessage(R.string.invite_judges_intro)
    }

    @Test
    fun onViewBoundShouldShowPermissionDeniedMessageWhenAppDoesNotHavePermissions() {
        setup(false, ParticipationType.CONTESTANT)
        presenter.onViewBound()
        verify<View>(mockView).showPermissionDeniedMessage(anyInt(), anyInt())
    }
}
