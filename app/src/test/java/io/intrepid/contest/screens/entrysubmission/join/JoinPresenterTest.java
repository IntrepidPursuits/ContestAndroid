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
    public void onEntryCodeTextViewChangedShouldHideSubmitButtonWhenCodeIsEmpty() {
        String newCode = "";

        presenter.onEntryCodeTextChanged(newCode);

        verify(mockView).hideSubmitButton();
        verify(mockView, never()).showSubmitButton();
    }

    @Test
    public void onEntryCodeTextViewChangedShouldShowSubmitButtonWhenCodeIsNotEmpty() {
        String newCode = "1";

        presenter.onEntryCodeTextChanged(newCode);

        verify(mockView).showSubmitButton();
        verify(mockView, never()).hideSubmitButton();
    }

    @Test
    public void onSubmitButtonClickedShouldShowEntryNameScreen() {
        presenter.onSubmitButtonClicked();
        verify(mockView).showEntryNameScreen();
    }
}