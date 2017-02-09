package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SelectContactsPresenterTest extends BasePresenterTest<SelectContactsPresenter> {
    @Mock
    SelectContactsContract.View mockView;

    @Before
    public void setup() {
        presenter = new SelectContactsPresenter(mockView, testConfiguration);
    }

    @Test
    public void onViewBoundShouldCheckForContactsPermissions() {
        presenter.onViewBound();
        verify(mockView).hasContactsPermissions();
    }

    @Test
    public void onViewBoundShouldDisplayContactListWhenAppHasPermissions() {
        when(mockView.hasContactsPermissions()).thenReturn(true);
        presenter.onViewBound();
        verify(mockView).displayContactList();
    }

    @Test
    public void onViewBoundShouldGoBackToPreviousScreenWhenAppDoesNotHavePermissions() {
        when(mockView.hasContactsPermissions()).thenReturn(false);
        presenter.onViewBound();
        verify(mockView).goBackToPreviousScreen();
    }
}
