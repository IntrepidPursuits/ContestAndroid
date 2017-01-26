package io.intrepid.contest.screens.entrysubmission.entryname;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.screens.entrysubmission.entryname.EntryNameContract.Presenter;
import io.intrepid.contest.screens.entrysubmission.entryname.EntryNameContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EntryNamePresenterTest extends BasePresenterTest<EntryNamePresenter> {
    @Mock
    View mockView;

    private Presenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new EntryNamePresenter(mockView, TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void onEntryNameTextViewChangedShouldHideNextButtonWhenNameIsNotEmpty() {
        String newText = "";

        presenter.onEntryNameTextChanged(newText);

        verify(mockView).hideEntryNameButton();
        verify(mockView, never()).showEntryNameButton();
    }

    @Test
    public void onEntryNameTextViewChangedShouldShowIconWhenNameIsNotEmpty() {
        String newText = "";

        presenter.onEntryNameTextChanged(newText);

        verify(mockView).showEntryNameIcon();
        verify(mockView, never()).hideEntryNameIcon();
    }

    @Test
    public void onEntryNameTextViewChangedShouldShowNextButtonWhenNameIsNotEmpty() {
        String newText = "1";

        presenter.onEntryNameTextChanged(newText);

        verify(mockView).showEntryNameButton();
        verify(mockView, never()).hideEntryNameButton();
    }

    @Test
    public void onEntryNameTextViewChangedShouldHideIconWhenNameIsNotEmpty() {
        String newText = "1";

        presenter.onEntryNameTextChanged(newText);

        verify(mockView).hideEntryNameIcon();
        verify(mockView, never()).showEntryNameIcon();
    }

    @Test
    public void onEntryNameSubmittedShouldShowEntryImageScreen() {
        String entryName = "Name";
        presenter.onEntryNameSubmitted(entryName);
        verify(mockView).showEntryImageScreen(any());
    }
}
