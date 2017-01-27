package io.intrepid.contest.screens.entrysubmission.join;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.screens.entrysubmission.join.JoinContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class JoinPresenterTest extends BasePresenterTest<JoinPresenter> {
    @Mock
    View mockView;

    private JoinContract.Presenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new JoinPresenter(mockView, TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void onEntryCodeTextViewChangedShouldDisableSubmitButtonWhenCodeIsEmpty() {
        String newCode = "";

        presenter.onEntryCodeTextChanged(newCode);

        verify(mockView).disableSubmitButton();
        verify(mockView, never()).enableSubmitButton();
    }

    @Test
    public void onEntryCodeTextViewChangedShouldEnableSubmitButtonWhenCodeIsNotEmpty() {
        String newCode = "1";

        presenter.onEntryCodeTextChanged(newCode);

        verify(mockView).enableSubmitButton();
        verify(mockView, never()).disableSubmitButton();
    }

    @Test
    public void onSubmitButtonClickedShouldShowEntryNameScreen() {
        presenter.onSubmitButtonClicked();
        verify(mockView).showEntryNameScreen();
    }
}