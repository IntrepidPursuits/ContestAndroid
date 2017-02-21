package io.intrepid.contest.screens.sendinvitations.invitationintro;

import org.junit.Test;
import org.mockito.Mock;

import io.intrepid.contest.R;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

public class InvitationRequestIntroPresenterTest extends BasePresenterTest<InvitationIntroPresenter> {
    @Mock
    InvitationIntroContract.View mockView;

    private void setup(boolean hasContactPermissions, ParticipationType participationType) {
        presenter = new InvitationIntroPresenter(mockView,
                                                 testConfiguration,
                                                 hasContactPermissions,
                                                 participationType);
    }

    @Test
    public void onViewBoundShouldShowSelectContestantsMessageWhenAppHasPermissionsAndIsInvitingContestants() {
        setup(true, ParticipationType.CONTESTANT);
        presenter.onViewBound();
        verify(mockView).showSelectContactsMessage(R.string.invite_contestants_intro);
    }

    @Test
    public void onViewBoundShouldShowSelectJudgesMessageWhenAppHasPermissionsAndIsInvitingJudges() {
        setup(true, ParticipationType.JUDGE);
        presenter.onViewBound();
        verify(mockView).showSelectContactsMessage(R.string.invite_judges_intro);
    }

    @Test
    public void onViewBoundShouldShowPermissionDeniedMessageWhenAppDoesNotHavePermissions() {
        setup(false, ParticipationType.CONTESTANT);
        presenter.onViewBound();
        verify(mockView).showPermissionDeniedMessage(anyInt(), anyInt());
    }
}
