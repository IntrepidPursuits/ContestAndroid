package io.intrepid.contest.screens.sendinvitations.invitationintro;

import org.junit.Test;
import org.mockito.Mock;

import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

public class InvitationRequestIntroPresenterTest extends BasePresenterTest<InvitationIntroPresenter> {
    @Mock
    InvitationIntroContract.View mockView;

    private void setupWithPermissions(boolean hasContactPermissions) {
        presenter = new InvitationIntroPresenter(mockView,
                                                 testConfiguration,
                                                 hasContactPermissions);
    }

    @Test
    public void onViewBoundShouldShowSelectContactsMessageWhenAppHasPermissions() {
        setupWithPermissions(true);
        presenter.onViewBound();
        verify(mockView).showSelectContactsMessage();
    }

    @Test
    public void onViewBoundShouldShowPermissionDeniedMessageWhenAppDoesNotHavePermissions() {
        setupWithPermissions(false);
        presenter.onViewBound();
        verify(mockView).showPermissionDeniedMessage(anyInt(), anyInt());
    }
}
