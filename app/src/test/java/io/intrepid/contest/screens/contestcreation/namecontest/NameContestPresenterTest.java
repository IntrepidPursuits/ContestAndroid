package io.intrepid.contest.screens.contestcreation.namecontest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.models.Contest;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NameContestPresenterTest {
    @Mock
    NameContestContract.View mockView;
    @Mock
    Contest.Builder mockContestBuilder;
    private NameContestPresenter nameContestPresenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        nameContestPresenter = spy(new NameContestPresenter(mockView,
                                                            TestPresenterConfiguration.createTestConfiguration(),
                                                            mockContestBuilder));
    }

    @Test
    public void onContestTitleUpdatedShouldCauseViewToShowNextScreen() {
        String VALID_TEXT = "ContestName";
        nameContestPresenter.onContestTitleUpdated(VALID_TEXT);
        verify(mockView).showNextScreen();
    }

    @Test
    public void onViewCreatedShouldTriggerViewToHideNextButton(){
        nameContestPresenter.onViewCreated();
        verify(mockView).setNextEnabled(false);
    }

    @Test
    public void onNextValidatedShouldCauseViewToEnableNext() {
        nameContestPresenter.onNextValidated();
        verify(mockView).setNextEnabled(true);
    }

    @Test
    public void onNextInValidatedShouldCauseViewToDisableNext() {
        nameContestPresenter.onNextInvalidated();
        verify(mockView).setNextEnabled(false);
    }

    @Test
    public void onTextChangedShouldCauseViewToEnableNext() {
        String validText = " A";

        nameContestPresenter.onTextChanged(validText);

        assertFalse(nameContestPresenter.isEmpty(validText));
        verify(mockView).setNextEnabled(true);
    }

    @Test
    public void onTextChangedShouldCauseViewToDisableNextWhenEmptyTextIsEntered() {
        String emptyText = "      ";
        nameContestPresenter.onTextChanged(emptyText);

        assertTrue(nameContestPresenter.isEmpty(emptyText));
        verify(mockView).setNextEnabled(false);
    }
}

