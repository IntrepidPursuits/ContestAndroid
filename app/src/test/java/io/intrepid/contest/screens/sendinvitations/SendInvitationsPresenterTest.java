package io.intrepid.contest.screens.sendinvitations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.R;
import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendInvitationsPresenterTest extends BasePresenterTest<SendInvitationsPresenter> {
    @Mock
    SendInvitationsContract.View mockView;

    @Before
    public void setup() {
        presenter = new SendInvitationsPresenter(mockView, testConfiguration);
    }

    @Test
    public void onViewBoundShouldCheckForContactsPermissions() {
        presenter.onViewBound();
        verify(mockView).hasContactsPermissions();
    }

    @Test
    public void onViewBoundShouldRequestContactsPermissionsWhenAppDoesNotHavePermissions() {
        when(mockView.hasContactsPermissions()).thenReturn(false);
        presenter.onViewBound();
        verify(mockView).requestContactsPermissions();
    }

    @Test
    public void onViewBoundShouldNotRequestContactsPermissionsWhenAppHasPermissions() {
        when(mockView.hasContactsPermissions()).thenReturn(true);
        presenter.onViewBound();
        verify(mockView, never()).requestContactsPermissions();
    }

    @Test
    public void onSelectContactsButtonClickedShouldShowSelectContactsScreen() {
        presenter.onSelectContactsButtonClicked();
        verify(mockView).showSelectContactsScreen();
    }

    @Test
    public void onContactsPermissionsResultShouldShowSelectContactsButtonWhenPermissionHasBeenGranted() {
        presenter.onContactsPermissionsResult(true);
        verify(mockView).showSelectContactsButton(true);
    }

    @Test
    public void onContactsPermissionsResultShouldShowSelectContactsMessageWhenPermissionHasBeenGranted() {
        presenter.onContactsPermissionsResult(true);

        verify(mockView).showSelectContactsMessage();
        verify(mockView, never()).showPermissionDeniedMessage();
    }

    @Test
    public void onContactsPermissionsResultShouldHideSelectContactsButtonWhenPermissionHasBeenDenied() {
        presenter.onContactsPermissionsResult(false);
        verify(mockView).showSelectContactsButton(false);
    }

    @Test
    public void onContactsPermissionsResultShouldShowPermissionDeniedMessageWhenPermissionHasBeenDenied() {
        presenter.onContactsPermissionsResult(false);

        verify(mockView).showPermissionDeniedMessage();
        verify(mockView, never()).showSelectContactsMessage();
    }

    @Test
    public void onCreateOptionsMenuShouldHideSendInvitationsMenuItem() {
        presenter.onCreateOptionsMenu();
        verify(mockView).showSendInvitationsMenuItem(false);
    }

    @Test
    public void onCreateOptionsMenuShouldShowSendInvitationsSkipMenuItem() {
        presenter.onCreateOptionsMenu();
        verify(mockView).showSendInvitationsSkipMenuItem(true);
    }

    @Test
    public void onOptionsItemSelectedShouldShowMessageWhenItemIsSendInvitations() {
        presenter.onOptionsItemSelected(R.id.send_invitations_menu_action);
        verify(mockView).showMessage(anyString());
    }

    @Test
    public void onOptionsItemSelectedShouldShowMessageWhenItemIsSendInvitationsSkip() {
        presenter.onOptionsItemSelected(R.id.send_invitations_skip_menu_action);
        verify(mockView).showMessage(anyString());
    }
}
